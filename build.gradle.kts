import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.kotlinCompose) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.vanniktech.maven.publish) apply false

  alias(libs.plugins.spotless)
}

allprojects {
  repositories {
    google()
    mavenCentral()
    mavenLocal()
  }
}

spotless {
  kotlin {
    target("**/*.kt")
    targetExclude("!**/build/**/*.*")
    ktlint(libs.versions.ktlint.get())
      .editorConfigOverride(
        mapOf(
          "indent_size" to "2",
          "max_line_length" to "120",
          "ij_kotlin_allow_trailing_comma" to "true",
          "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
          "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
        ),
      )
    trimTrailingWhitespace()
    endWithNewline()
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    ktlint(libs.versions.ktlint.get())
      .editorConfigOverride(
        mapOf(
          "indent_size" to "2",
          "max_line_length" to "120",
          "ij_kotlin_allow_trailing_comma_on_declaration_site" to "true",
        ),
      )
    trimTrailingWhitespace()
    endWithNewline()
  }
}

subprojects {
  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }
}
