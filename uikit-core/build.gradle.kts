plugins {
  alias(libs.plugins.kotlinCompose)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.vanniktech.maven.publish)
}

android {
  namespace = "ru.kode.uikit.core"
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

dependencies {
  api(libs.compose.ui)
  api(libs.compose.runtime)
  api(libs.compose.foundation)
  coreLibraryDesugaring(libs.android.jdk.desugar)
}
