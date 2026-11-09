package ru.kode.uikit.sample

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.GraphicsMode
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class PreviewScreenshotTest(private val preview: ComposablePreview<AndroidPreviewInfo>) {
  @get:Rule
  val compose = createComposeRule()

  @Test
  fun snapshot() {
    // Paused clock: infinite animations (loading, shimmer) would never let the UI go idle.
    compose.mainClock.autoAdvance = false
    compose.setContent { preview() }
    compose.mainClock.advanceTimeBy(FRAME_TIME_MS)
    compose.onRoot().captureRoboImage("src/test/snapshots/${preview.methodName}.png")
  }

  companion object {
    @JvmStatic
    @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
    fun previews(): List<ComposablePreview<AndroidPreviewInfo>> = AndroidComposablePreviewScanner()
      .scanPackageTrees("ru.kode.uikit.sample.previews")
      .getPreviews()
  }
}

internal const val FRAME_TIME_MS = 1_000L
