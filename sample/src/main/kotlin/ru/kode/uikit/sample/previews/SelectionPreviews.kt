package ru.kode.uikit.sample.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.Checkbox
import ru.kode.uikit.components.RadioButton
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Selection"

@ShowkaseComposable(name = "Checkbox — states", group = GROUP)
@Preview
@Composable
fun CheckboxStatesPreview() {
  PreviewTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      Checkbox(checked = true, onClick = {})
      Checkbox(checked = false, onClick = {})
      Checkbox(checked = true, error = true, onClick = {})
      Checkbox(checked = true, enabled = false, onClick = {})
    }
  }
}

@ShowkaseComposable(name = "RadioButton — states", group = GROUP)
@Preview
@Composable
fun RadioButtonStatesPreview() {
  PreviewTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      RadioButton(selected = true, onClick = {})
      RadioButton(selected = false, onClick = {})
      RadioButton(selected = true, error = true, onClick = {})
      RadioButton(selected = true, enabled = false, onClick = {})
    }
  }
}

/** Slot check: an app-drawn box replaces the backend's, so both backends render this identically. */
@ShowkaseComposable(name = "Checkbox — custom box slot", group = GROUP)
@Preview
@Composable
fun CheckboxCustomBoxPreview() {
  PreviewTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      listOf(true, false).forEach { checked ->
        Checkbox(
          checked = checked,
          onClick = {},
          box = { state ->
            val color = if (state.error) Color.Red else Color(0xFF3F51B5)
            Box(
              Modifier
                .fillMaxSize()
                .border(2.dp, color, CircleShape)
                .background(if (state.checked) color else Color.Transparent, CircleShape),
            )
          },
        )
      }
    }
  }
}
