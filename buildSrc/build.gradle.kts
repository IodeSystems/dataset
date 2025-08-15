repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
}

plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(libs.gradle.kotlin.plugin)
  implementation(libs.gradle.dokka.plugin)
  implementation(libs.gradle.nexus.publish.plugin)
}
