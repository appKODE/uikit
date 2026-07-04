plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinCompose)
  alias(libs.plugins.ksp)
}

android {
  namespace = "ru.kode.uikit.sample"
  compileSdk = 36

  defaultConfig {
    applicationId = "ru.kode.uikit.sample"
    // The gallery is a demo host, not the published library; activity-compose requires minSdk 23.
    minSdk = 23
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"
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
  implementation(project(":uikit-components"))
  implementation(libs.bundles.compose)
  implementation(libs.androidx.activity.compose)

  implementation(libs.showkase)
  implementation(libs.showkase.annotation)
  ksp(libs.showkase.processor)

  coreLibraryDesugaring(libs.android.jdk.desugar)
}
