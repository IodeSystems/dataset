import com.iodesystems.build.Antlr.antlr
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.iodesystems.dataset"
version = "9.0.1-SNAPSHOT"
description =
  "dataset is a simple query language parser that converts user queries to SQL conditions (using Antlr4 and JOOQ) with an aim for least surprise."

repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  id("release-conventions")
  id("nl.littlerobots.version-catalog-update") version "1.0.1"
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
  javadoc {
    dependsOn(parser, lexer)
  }
  compileKotlin {
    dependsOn(parser, lexer)
  }
  kotlinSourcesJar {
    dependsOn(parser, lexer)
  }
  withType<org.gradle.jvm.tasks.Jar>().configureEach {
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


java {
  withSourcesJar()
  withJavadocJar()
}
signing {
  isRequired = requireSign
  useGpgCmd()
  sign(publishing.publications)
}

private fun joinName(p: Project): String {
  var name = p.name
  val pp = p.parent
  if (pp != null && pp != rootProject) {
    name = name + "-" + joinName(pp)
  }
  return name
}
publishing {
  publications {
    create<MavenPublication>("java") {
      from(components["java"])
    }
  }
  afterEvaluate {
    publications {
      withType<MavenPublication> {
        artifactId = joinName(project)
        pom {
          name.set(project.name)
          description.set(project.description.let {
            if (it.isNullOrBlank()) rootProject.description
            else it
          })
          url.set("https://github.com/iodesystems/dataset")
          licenses {
            license {
              name.set("MIT License")
              url.set("https://www.opensource.org/licenses/mit-license.php")
            }
          }
          developers {
            developer {
              organization.set("iodesystems")
              organizationUrl.set("https://iodesystems.com")
              id.set("nthalk")
              name.set("Carl Taylor")
              email.set("carl@etaylor.me")
              roles.add("owner")
              roles.add("developer")
              timezone.set("-8")
            }
          }
          scm {
            connection.set("scm:git:git@github.com:iodesystems/dataset.git")
            developerConnection.set("scm:git:git@github.com:iodesystems/dataset.git")
            url.set("https://github.com/iodesystems/dataset")
            tag.set("${rootProject.version}")
          }
        }
      }
    }
  }
}
val requireSign = !rootProject.hasProperty("skipSigning")
val signingTasks = tasks.withType<Sign>()
signingTasks.configureEach {
  onlyIf { requireSign }
}
tasks.withType<AbstractPublishToMaven>().configureEach {
  dependsOn(signingTasks)
}


