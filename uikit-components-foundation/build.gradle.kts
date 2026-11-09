import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKmpLibrary)
  alias(libs.plugins.kotlinCompose)
  alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
  jvmToolchain(17)

  android {
    namespace = "ru.kode.uikit.components.foundation"
    compileSdk = 36
    minSdk = 21
  }
  jvm()
  iosArm64()
  iosSimulatorArm64()
  js { browser { testTask { enabled = false } } }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs { browser { testTask { enabled = false } } }

  // Every non-Android target shares the plain sheet dialog (nonAndroidMain).
  applyDefaultHierarchyTemplate {
    common {
      group("nonAndroid") {
        withJvm()
        withIos()
        withJs()
        withWasmJs()
      }
    }
  }

  sourceSets {
    commonMain.dependencies {
      api(project(":uikit-core"))
      implementation(libs.cmp.material.ripple)
      implementation(libs.androidx.annotation)
    }
    androidMain.dependencies {
      implementation(libs.compose.material.ripple)
    }
  }
}
