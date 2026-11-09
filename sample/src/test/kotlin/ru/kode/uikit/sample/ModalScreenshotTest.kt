package ru.kode.uikit.sample

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.GraphicsMode
import ru.kode.uikit.components.ModalBottomSheet
import ru.kode.uikit.components.TextFieldDatePicker
import ru.kode.uikit.components.rememberDatePickerState
import ru.kode.uikit.sample.previews.PreviewDate

// Dialogs/popups live in their own window, so these use full-screen capture instead of the preview scan.
@OptIn(ExperimentalRoborazziApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ModalScreenshotTest {
  @get:Rule
  val compose = createComposeRule()

  @Test
  fun modalBottomSheet() {
    compose.setContent {
      PreviewTheme {
        ModalBottomSheet(onDismissRequest = {}) {
          repeat(5) { Text("Bottom sheet row ${it + 1}") }
        }
      }
    }
    compose.waitForIdle()
    captureScreenRoboImage("src/test/snapshots/ModalBottomSheet.png")
  }

  @Test
  fun modalDatePicker() {
    compose.setContent {
      PreviewTheme {
        TextFieldDatePicker(
          state = rememberDatePickerState(initialSelectedDate = PreviewDate),
          onDateConfirm = {},
          label = "Birth date",
        )
      }
    }
    compose.onAllNodesWithText("15.01.2026")[0].performClick()
    compose.waitForIdle()
    captureScreenRoboImage("src/test/snapshots/ModalDatePicker.png")
  }
}
