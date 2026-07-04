package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.TextFieldDatePicker
import ru.kode.uikit.components.rememberDatePickerState
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Date"

@ShowkaseComposable(name = "TextFieldDatePicker — states", group = GROUP)
@Preview
@Composable
fun TextFieldDatePickerPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
      TextFieldDatePicker(
        state = rememberDatePickerState(),
        onDateConfirm = {},
        label = "Birth date",
        helper = "Tap to choose",
      )
      TextFieldDatePicker(
        state = rememberDatePickerState(),
        onDateConfirm = {},
        label = "Birth date",
        helper = "Required field",
        error = true,
      )
      TextFieldDatePicker(
        state = rememberDatePickerState(),
        onDateConfirm = {},
        label = "Birth date",
        enabled = false,
      )
    }
  }
}
