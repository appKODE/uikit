package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.Button
import ru.kode.uikit.sample.PreviewButtonConfiguration
import ru.kode.uikit.sample.PreviewPrimaryButtonColors
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Buttons"

@ShowkaseComposable(name = "Primary — enabled", group = GROUP)
@Preview
@Composable
fun ButtonEnabledPreview() {
  PreviewTheme {
    Button(
      configuration = PreviewButtonConfiguration,
      colors = PreviewPrimaryButtonColors,
      onClick = {},
    ) {
      with(Button) { Text("Sign in") }
    }
  }
}

@ShowkaseComposable(name = "Primary — disabled", group = GROUP)
@Preview
@Composable
fun ButtonDisabledPreview() {
  PreviewTheme {
    Button(
      configuration = PreviewButtonConfiguration,
      colors = PreviewPrimaryButtonColors,
      enabled = false,
      onClick = {},
    ) {
      with(Button) { Text("Sign in") }
    }
  }
}

@ShowkaseComposable(name = "Primary — loading", group = GROUP)
@Preview
@Composable
fun ButtonLoadingPreview() {
  PreviewTheme {
    Button(
      configuration = PreviewButtonConfiguration,
      colors = PreviewPrimaryButtonColors,
      loading = true,
      onClick = {},
    ) {
      with(Button) { Text("Sign in") }
    }
  }
}

@ShowkaseComposable(name = "States — column", group = GROUP)
@Preview
@Composable
fun ButtonStatesPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Button(
        configuration = PreviewButtonConfiguration,
        colors = PreviewPrimaryButtonColors,
        onClick = {},
      ) { with(Button) { Text("Enabled") } }
      Button(
        configuration = PreviewButtonConfiguration,
        colors = PreviewPrimaryButtonColors,
        loading = true,
        onClick = {},
      ) { with(Button) { Text("Loading") } }
      Button(
        configuration = PreviewButtonConfiguration,
        colors = PreviewPrimaryButtonColors,
        enabled = false,
        onClick = {},
      ) { with(Button) { Text("Disabled") } }
    }
  }
}
