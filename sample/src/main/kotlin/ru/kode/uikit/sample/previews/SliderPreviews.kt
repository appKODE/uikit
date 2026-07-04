@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.MaxSlider
import ru.kode.uikit.components.MinSlider
import ru.kode.uikit.components.RangeSlider
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Sliders"

@ShowkaseComposable(name = "MinSlider", group = GROUP)
@Preview
@Composable
fun MinSliderPreview() {
  PreviewTheme {
    var value by remember { mutableStateOf(0.4f) }
    MinSlider(value = value, onValueChange = { value = it })
  }
}

@ShowkaseComposable(name = "MaxSlider", group = GROUP)
@Preview
@Composable
fun MaxSliderPreview() {
  PreviewTheme {
    var value by remember { mutableStateOf(0.6f) }
    MaxSlider(value = value, onValueChange = { value = it })
  }
}

@ShowkaseComposable(name = "RangeSlider", group = GROUP)
@Preview
@Composable
fun RangeSliderPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      var range by remember { mutableStateOf(0.2f..0.8f) }
      RangeSlider(value = range, onValueChange = { range = it })
      Text("Disabled")
      var disabledRange by remember { mutableStateOf(0.3f..0.7f) }
      RangeSlider(value = disabledRange, onValueChange = { disabledRange = it }, enabled = false)
    }
  }
}
