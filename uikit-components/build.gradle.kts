plugins {
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.kotlinCompose)
  alias(libs.plugins.androidLibrary)
  `maven-publish`
}

android {
  namespace = "ru.kode.uikit.components"
  compileSdk = 34

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

  publishing {
    singleVariant("release") {
      withSourcesJar()
      withJavadocJar()
    }
  }
}

publishing {
  publications {
    register<MavenPublication>("release") {
      afterEvaluate {
        from(components["release"])
      }
    }
  }
}

dependencies {
  implementation(libs.coroutines.android)
  implementation(libs.bundles.compose)
  coreLibraryDesugaring(libs.android.jdk.desugar)
}
