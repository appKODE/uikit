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
    namespace = "ru.kode.uikit.components"
    compileSdk = 36
    minSdk = 21
  }
  jvm()
  iosArm64()
  iosSimulatorArm64()
  js { browser { testTask { enabled = false } } }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs { browser { testTask { enabled = false } } }

  sourceSets {
    commonMain.dependencies {
      api(project(":uikit-core"))
      implementation(libs.cmp.material3)
      implementation(libs.androidx.annotation)
    }
    androidMain.dependencies {
      // CMP material3 already resolves to androidx material3 on Android; pin it explicitly.
      implementation(libs.compose.material3)
    }
  }
}
