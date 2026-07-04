package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.TextField
import ru.kode.uikit.components.TextFieldArea
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Inputs"

@ShowkaseComposable(name = "TextField — states", group = GROUP)
@Preview
@Composable
fun TextFieldStatesPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      TextField(
        value = TextFieldValue(""),
        onValueChange = {},
        placeholder = { Text("Placeholder") },
      )
      TextField(
        value = TextFieldValue("Filled value"),
        onValueChange = {},
      )
      TextField(
        value = TextFieldValue("Invalid value"),
        onValueChange = {},
        isError = true,
      )
      TextField(
        value = TextFieldValue("Disabled value"),
        onValueChange = {},
        enabled = false,
      )
    }
  }
}

@ShowkaseComposable(name = "TextFieldArea — states", group = GROUP)
@Preview
@Composable
fun TextFieldAreaStatesPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      TextFieldArea(
        value = TextFieldValue(""),
        onValueChange = {},
        placeholder = { Text("Multi-line placeholder") },
      )
      TextFieldArea(
        value = TextFieldValue("Some long\nmulti-line content"),
        onValueChange = {},
        isError = true,
      )
    }
  }
}
