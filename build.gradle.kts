import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.kotlinCompose) apply false
  alias(libs.plugins.androidLibrary) apply false

  alias(libs.plugins.spotless)

  `maven-publish`
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
          "ktlint_standard_trailing-comma-on-call-site" to "disabled",
          "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
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
      jvmTarget.set(JvmTarget.JVM_1_8)
    }
  }

  plugins.withType<MavenPublishPlugin> {
    apply(plugin = "org.gradle.signing")

    configure<PublishingExtension> {
      repositories {
        maven {
          name = "Central"
          val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
          val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
          val versionName: String by project
          url = if (versionName.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
          credentials {
            username = project.findProperty("NEXUS_USERTOKEN_NAME")?.toString()
            password = project.findProperty("NEXUS_USERTOKEN_PASSWORD")?.toString()
          }
        }
      }

      publications.withType<MavenPublication> {
        val versionName: String by project
        val pomGroupId: String by project
        groupId = pomGroupId
        version = versionName
        pom {
          val pomDescription: String by project
          val pomUrl: String by project
          val pomName: String by project
          description.set(pomDescription)
          url.set(pomUrl)
          name.set(pomName)
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

      configure<SigningExtension> {
        sign(publications)
      }
    }
  }
}
