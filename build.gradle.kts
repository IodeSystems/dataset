import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Duration

group = "com.iodesystems.dataset"
version = "7.0.3-SNAPSHOT"
description =
  "dataset is a simple query language parser that converts user queries to SQL conditions (using Antlr4 and JOOQ) with an aim for least surprise."

repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  kotlin("jvm")
  `java-library`
  `maven-publish`
  signing
  id("org.jetbrains.dokka-javadoc")
  id("io.github.gradle-nexus.publish-plugin")
}

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain(21)
}

tasks {
  fun Project.antlr(
    grammarFile: String,
    packageName: String = "$group.$name",
    outputDir: String = "$rootDir/src/gen/java",
    inputDir: String = "$rootDir/src/main/antlr4"
  ) {
    val pkgPath = packageName.replace(".", "/")

    val compileOnlyDepFiles = configurations.compileClasspath.get().files
    File(outputDir).mkdirs()

    val cmd = arrayOf(
      "java",
      "-classpath", compileOnlyDepFiles.joinToString(":") { it.absolutePath },
      "org.antlr.v4.Tool",
      "-o", "$outputDir/$pkgPath",
      "-package", packageName,
      "-lib", outputDir,
      "$inputDir/$grammarFile"
    )

    println("exec ~= ${cmd.joinToString(" ")}")
    val exec = Runtime.getRuntime().exec(cmd)
    val messages = BufferedReader(InputStreamReader(exec.errorStream)).use { err ->
      generateSequence { err.readLine() }.toList()
    }
    val result = exec.waitFor()

    if (result != 0) {
      throw RuntimeException(
        """
        | antlr exec ~= ${cmd.joinToString(" ")}
        | failed with code=${result} because:
        |${messages.joinToString("\n") { "    " + it.trim() }}
        """.trimMargin()
      )
    }
  }

  val lexer = register("generateLexer") {
    group = "antlr"
    doLast {
      antlr(
        grammarFile = "DataSetSearchLexer.g4",
        packageName = "com.iodesystems.db.query",
        outputDir = "$rootDir/src/main/java-generated"
      )
    }
  }
  val parser = register("generateParser") {
    group = "antlr"
    dependsOn(lexer)
    doLast {
      antlr(
        grammarFile = "DataSetSearchParser.g4",
        packageName = "com.iodesystems.db.query",
        outputDir = "$rootDir/src/main/java-generated"
      )
    }
  }
  compileKotlin {
    dependsOn(parser)
  }

}

sourceSets {
  main {
    java {
      srcDir("$rootDir/src/main/java-generated")
    }
  }
}

tasks.clean {
  delete("$rootDir/src/main/java-generated")
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
    freeCompilerArgs.add("-Xjsr305=strict")
    freeCompilerArgs.add("-java-parameters")
  }
}

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
  dependsOn(tasks.dokkaGeneratePublicationJavadoc)
  archiveClassifier.set("javadoc")
  from(tasks.dokkaGeneratePublicationJavadoc.get().outputDirectory)
}
publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifact(javadocJar)
      artifact(tasks.kotlinSourcesJar) {
        classifier = "sources"
      }
      pom {
        name.set(project.name)
        description.set(project.description)
        url.set("https://iodesystems.github.io/dataset/")
        licenses {
          license {
            name.set("MIT License")
            url.set("https://www.opensource.org/licenses/mit-license.php")
          }
        }
        developers {
          developer {
            id.set("nthalk")
            name.set("Carl Taylor")
            email.set("carl@etaylor.me")
            roles.add("owner")
            roles.add("developer")
            timezone.set("-8")
          }
        }
        scm {
          connection.set("scm:git:git@github.com:IodeSystems/dataset.git")
          developerConnection.set("scm:git:git@github.com:IodeSystems/dataset.git")
          url.set("https://iodesystems.github.io/dataset/")
          tag.set("$version")
        }
      }
    }
  }
}

nexusPublishing {
  transitionCheckOptions {
    maxRetries.set(300)
    delayBetween.set(Duration.ofSeconds(10))
  }
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

dependencies {
  // For building
  compileClasspath(libs.antlr4)
  compileClasspath(libs.antlr4.runtime)
  implementation(libs.slf4j.api)

  implementation(libs.kotlin.stdlib.jdk8)
  implementation(libs.kotlin.reflect)
  implementation(libs.antlr4.runtime)
  implementation(libs.jooq)
  testImplementation(Kotlin.test.junit)
  testImplementation(libs.junit)
  testImplementation(libs.h2database)
}

signing {
  useGpgCmd()
  sign(publishing.publications)
}

tasks.register("releaseStripSnapshotCommitAndTag") {
  group = "release"
  doLast {
    val status = "git status --porcelain".bash().trim()
    if (status.isNotEmpty()) {
      throw GradleException("There are changes in the working directory:\n$status")
    }
    val oldVersion = version.toString()
    val newVersion = oldVersion.removeSuffix("-SNAPSHOT")
    writeVersion(newVersion)
    // Validate build
    "./gradlew clean build test".bash()
    "git add build.gradle.kts".bash()
    "git commit -m 'Release $newVersion'".bash()
    "git tag -a v$newVersion -m 'Release $newVersion'".bash()
  }
}
tasks.register("releaseRevert") {
  group = "release"
  doLast {
    val oldVersion = version.toString()
    val newVersion = "$oldVersion-SNAPSHOT"
    writeVersion(newVersion, oldVersion)
    "git reset --hard HEAD~1".bash()
    "git tag -d v$oldVersion".bash()
    println("Reverted to $newVersion")
  }
}
tasks.register("releasePublish") {
  dependsOn(tasks.clean)
  dependsOn(tasks.build)
  dependsOn(tasks.publish)
  dependsOn(tasks.closeAndReleaseStagingRepositories)
  doLast {
    val oldVersion = version.toString()
    val newVersion = generateVersion("dev")
    writeVersion(newVersion, oldVersion)
    "git add build.gradle.kts".bash()
    "git commit -m 'Prepare next development iteration: $newVersion'".bash()
    "git push".bash()
    "git push --tags".bash()
  }
}
tasks.register("releasePrepareNextDevelopmentIteration") {
  doLast {
    val oldVersion = version.toString()
    val newVersion = generateVersion("dev")
    writeVersion(newVersion, oldVersion)
    "git add build.gradle.kts".bash()
    "git commit -m 'Prepare next development iteration: $newVersion'".bash()
    "git push".bash()
  }
}

fun generateVersion(updateMode: String): String {
  properties["overrideVersion"].let {
    if (it != null) {
      return it as String
    }
  }

  val version = properties["version"] as String
  val nonSnapshotVersion = version.removeSuffix("-SNAPSHOT")

  val (oldMajor, oldMinor, oldPatch) = nonSnapshotVersion.split(".").map(String::toInt)
  var (newMajor, newMinor, newPatch) = arrayOf(oldMajor, oldMinor, 0)

  when (updateMode) {
    "major" -> newMajor = (oldMajor + 1).also { newMinor = 0 }
    "minor" -> newMinor = oldMinor + 1
    "dev" -> newPatch = oldPatch + 1
    else -> newPatch = oldPatch + 1
  }
  if (updateMode == "dev" || nonSnapshotVersion != version) {
    return "$newMajor.$newMinor.$newPatch-SNAPSHOT"
  }
  return "$newMajor.$newMinor.$newPatch"
}

fun writeVersion(newVersion: String, oldVersion: String = version.toString()) {
  val oldContent = buildFile.readText()
  val newContent = oldContent.replace("""= "$oldVersion"""", """= "$newVersion"""")
  buildFile.writeText(newContent)
}

private fun String.bash(): String {
  val process = ProcessBuilder(
    "bash", "-c", this
  ).start()
  var content = ""
  val er = Thread {
    process.errorStream.reader().useLines { lines ->
      lines.forEach {
        println(it)
      }
    }
  }
  val out = Thread {
    content = process.inputStream.reader().useLines { lines ->
      lines.map {
        println(it)
        it
      }.joinToString("\n")
    }
  }
  er.start()
  out.start()
  process.waitFor().also { code ->
    er.join()
    out.join()
    if (code != 0) {
      throw GradleException("Failed ($code) to execute command: $this")
    }
  }
  return content
}
