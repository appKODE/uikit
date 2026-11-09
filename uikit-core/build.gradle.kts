import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKmpLibrary)
  alias(libs.plugins.kotlinCompose)
  alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
  jvmToolchain(17)

  android {
    namespace = "ru.kode.uikit.core"
    compileSdk = 36
    minSdk = 21
  }
  jvm()
  iosArm64()
  iosSimulatorArm64()
  js { browser { testTask { enabled = false } } }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs { browser { testTask { enabled = false } } }

  // android + jvm share the java.time-based PlatformDates actual (jvmAndroidMain).
  applyDefaultHierarchyTemplate {
    common {
      group("jvmAndroid") {
        withJvm()
        withCompilations { it.platformType == KotlinPlatformType.androidJvm }
      }
    }
  }

  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }

  sourceSets {
    commonMain.dependencies {
      api(libs.cmp.ui)
      api(libs.cmp.runtime)
      api(libs.cmp.foundation)
      api(libs.kotlinx.datetime)
      implementation(libs.androidx.annotation)
    }
    androidMain.dependencies {
      // Pin the exact Jetpack Compose version Android consumers got before the KMP move.
      api(libs.compose.ui)
      api(libs.compose.runtime)
      api(libs.compose.foundation)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
  }
}
