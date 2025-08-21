import com.iodesystems.build.Antlr.antlr
import com.iodesystems.build.Bash.bash
import com.iodesystems.build.Release
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.Duration

group = "com.iodesystems.dataset"
version = "8.0.8-SNAPSHOT"
description =
  "dataset is a simple query language parser that converts user queries to SQL conditions (using Antlr4 and JOOQ) with an aim for least surprise."

repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  id("nl.littlerobots.version-catalog-update") version "1.0.0"
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
  val antlrOutDir = "$rootDir/src/main/java-generated"
  val antlrInputDir = "$rootDir/src/main/antlr4"
  val lexer = register("generateLexer") {
    group = "antlr"
    val grammarFile = "DataSetSearchLexer.g4"
    val compileOnlyDepFiles = configurations.getByName("compileClasspath").asPath
    inputs.dir(antlrInputDir)
    outputs.dir(antlrOutDir)
    doLast {
      antlr(
        grammarFile = grammarFile,
        packageName = "com.iodesystems.db.query",
        compileOnlyDepFiles = compileOnlyDepFiles,
        outputDir = antlrOutDir,
        inputDir = antlrInputDir
      )
    }
  }
  val parser = register("generateParser") {
    group = "antlr"
    val grammarFile = "DataSetSearchParser.g4"
    dependsOn(lexer)
    val compileOnlyDepFiles = configurations.getByName("compileClasspath").asPath
    inputs.dir(antlrInputDir)
    outputs.dir(antlrOutDir)
    doLast {
      antlr(
        grammarFile = grammarFile,
        packageName = "com.iodesystems.db.query",
        compileOnlyDepFiles = compileOnlyDepFiles,
        outputDir = antlrOutDir,
        inputDir = antlrInputDir
      )
    }
  }
  dokkaGeneratePublicationJavadoc {
    dependsOn(parser, lexer)
  }
  compileKotlin {
    dependsOn(parser, lexer)
  }
  kotlinSourcesJar {
    dependsOn(parser, lexer)
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
      nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
      snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
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
  testImplementation(libs.junit)
  testImplementation(libs.h2database)
}

signing {
  useGpgCmd()
  sign(publishing.publications)
}

tasks.register("versionGet") {
  group = "release"
  val version = version
  doLast {
    println(version)
  }
}
tasks.register("versionSet") {
  group = "release"
  val overrideVersion = properties["overrideVersion"]
  val updateMode = properties["updateMode"]!!.toString()
  val version = properties["version"]!!.toString()
  doLast {
    val newVersion = Release.generateVersion(version, updateMode, overrideVersion?.toString())
    Release.writeVersion(newVersion, version)
  }
}


tasks.register("releaseStripSnapshotCommitAndTag") {
  dependsOn(tasks.test)
  group = "release"
  val version = properties["version"]!!.toString()
  doLast {
    val status = "git status --porcelain".bash().output.trim()
    if (status.isNotEmpty()) {
      throw GradleException("There are changes in the working directory:\n$status")
    }
    val oldVersion = version
    val newVersion = oldVersion.removeSuffix("-SNAPSHOT")
    Release.writeVersion(newVersion, oldVersion)
    // Validate build
    "git add build.gradle.kts".bash()
    "git commit -m 'Release $newVersion'".bash()
    "git tag -a v$newVersion -m 'Release $newVersion'".bash()
  }
}
tasks.register("releaseRevert") {
  group = "release"
  val version = properties["version"]!!.toString()
  doLast {
    val oldVersion = version
    val newVersion = "$oldVersion-SNAPSHOT"
    Release.writeVersion(newVersion, oldVersion)
    "git reset --hard HEAD~1".bash()
    "git tag -d v$oldVersion".bash()
    println("Reverted to $newVersion")
  }
}
tasks.register("releasePublish") {
  group = "release"
  dependsOn(tasks.clean, tasks.build, tasks.publish, tasks.closeAndReleaseStagingRepositories)
  val overrideVersion = properties["overrideVersion"]?.toString()
  val version = properties["version"]!!.toString()
  doLast {
    val oldVersion = version
    val newVersion = Release.generateVersion(version, "dev", overrideVersion)
    Release.writeVersion(newVersion, oldVersion)
    "git add build.gradle.kts".bash()
    "git commit -m 'Prepare next development iteration: $newVersion'".bash()
    "git push".bash()
    "git push --tags".bash()
  }
}
tasks.register("releasePrepareNextDevelopmentIteration") {
  group = "release"
  val overrideVersion = properties["overrideVersion"]?.toString()
  val version = properties["version"]!!.toString()
  doLast {
    val oldVersion = version
    val newVersion = Release.generateVersion(version, "dev", overrideVersion)
    Release.writeVersion(newVersion, oldVersion)
    "git add build.gradle.kts".bash()
    "git commit -m 'Prepare next development iteration: $newVersion'".bash()
    "git push".bash()
  }
}


