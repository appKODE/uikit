plugins {
  alias(libs.plugins.kotlinCompose)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.vanniktech.maven.publish)
}

android {
  namespace = "ru.kode.uikit.components"
  compileSdk = 36

  defaultConfig {
    minSdk = 21
    aarMetadata {
      minCompileSdk = 21
    }
    val versionName: String by project
    version = versionName
  }

  buildFeatures {
    compose = true
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlin {
    jvmToolchain(17)
  }
}

mavenPublishing {
  publishToMavenCentral()
  signAllPublications()

  val versionName: String by project
  val pomGroupId: String by project
  coordinates(pomGroupId, "uikit-components", versionName)

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

dependencies {
  implementation(libs.coroutines.android)
  implementation(libs.bundles.compose)
  coreLibraryDesugaring(libs.android.jdk.desugar)
}
