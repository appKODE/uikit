import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.kotlinCompose) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.ksp) apply false
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
    targetExclude("**/build/**", ".claude/**")
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
    targetExclude(".claude/**")
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

  pluginManager.withPlugin("com.vanniktech.maven.publish") {
    configure<MavenPublishBaseExtension> {
      publishToMavenCentral()
      signAllPublications()

      val versionName: String by project
      val pomGroupId: String by project
      // Artifact id = module name: uikit-core, uikit-components, uikit-components-foundation.
      coordinates(pomGroupId, project.name, versionName)

      pom {
        val pomDescription: String by project
        val pomUrl: String by project
        val pomName: String by project
        name.set(pomName)
        description.set(pomDescription)
        url.set(pomUrl)
        scm {
          val pomScmUrl: String by project
          val pomScmConnection: String by project
          val pomScmDevConnection: String by project
          url.set(pomScmUrl)
          connection.set(pomScmConnection)
          developerConnection.set(pomScmDevConnection)
        }
        licenses {
          license {
            val pomLicenseName: String by project
            val pomLicenseUrl: String by project
            val pomLicenseDist: String by project
            name.set(pomLicenseName)
            url.set(pomLicenseUrl)
            distribution.set(pomLicenseDist)
          }
        }
        developers {
          developer {
            val pomDeveloperId: String by project
            val pomDeveloperName: String by project
            id.set(pomDeveloperId)
            name.set(pomDeveloperName)
          }
        }
      }
    }
  }
}
