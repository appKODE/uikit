package ru.kode.uikit.sample.previews

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.SegmentedControlContainer
import ru.kode.uikit.components.SegmentedControlToggle
import ru.kode.uikit.sample.PreviewTheme
import ru.kode.uikit.sample.PreviewTransparentSegmentedControlContainerColors
import ru.kode.uikit.sample.previewTransparentSegmentedControlToggleConfiguration

private const val GROUP = "Segmented control"

private val TABS = listOf("Day", "Week", "Month")

@ShowkaseComposable(name = "Container — selected segment", group = GROUP)
@Preview
@Composable
fun SegmentedControlSelectedOneDisabledPreview() {
  PreviewTheme {
    var selected by remember { mutableStateOf(TABS.first()) }
    SegmentedControlContainer(modifier = Modifier.fillMaxWidth()) {
      TABS.forEach { title ->
        toggle(key = title, selected = title == selected, onClick = { selected = title }) {
          Text(text = title)
        }
      }
    }
  }
}

@ShowkaseComposable(name = "Container — selected segment, one disabled", group = GROUP)
@Preview
@Composable
fun SegmentedControlSelectedPreview() {
  PreviewTheme {
    var selected by remember { mutableStateOf(TABS.first()) }
    SegmentedControlContainer(modifier = Modifier.fillMaxWidth()) {
      TABS.forEach { title ->
        toggle(
          key = title,
          enabled = title != "Week",
          selected = title == selected,
          onClick = { selected = title },
        ) {
          Text(text = title)
        }
      }
    }
  }
}

@ShowkaseComposable(name = "Container — disabled", group = GROUP)
@Preview
@Composable
fun SegmentedControlDisabledPreview() {
  PreviewTheme {
    SegmentedControlContainer(modifier = Modifier.fillMaxWidth(), enabled = false) {
      TABS.forEachIndexed { index, title ->
        toggle(key = title, selected = index == 0, enabled = false, onClick = {}) {
          Text(text = title)
        }
      }
    }
  }
}

@ShowkaseComposable(name = "Container — content wrapped width", group = GROUP)
@Preview
@Composable
fun SegmentedControlWrapWidthPreview() {
  PreviewTheme {
    var on by remember { mutableStateOf(true) }
    SegmentedControlContainer {
      toggle(key = "on", selected = on, onClick = { on = true }) { Text(text = "On") }
      toggle(key = "off", selected = !on, onClick = { on = false }) { Text(text = "Off") }
    }
  }
}

@ShowkaseComposable(name = "Toggle — states", group = GROUP)
@Preview
@Composable
fun SegmentedControlToggleStatesPreview() {
  PreviewTheme {
    var leftSelected by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SegmentedControlToggle(selected = leftSelected, onClick = { leftSelected = true }) {
          Text(text = if (leftSelected) "Selected" else "Unselected")
        }
        SegmentedControlToggle(selected = !leftSelected, onClick = { leftSelected = false }) {
          Text(text = if (leftSelected) "Unselected" else "Selected")
        }
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SegmentedControlToggle(selected = true, enabled = false, onClick = {}) {
          Text(text = "Selected, disabled")
        }
        SegmentedControlToggle(selected = false, enabled = false, onClick = {}) {
          Text(text = "Unselected, disabled")
        }
      }
    }
  }
}

@ShowkaseComposable(name = "Transparent container — grey deselected", group = GROUP)
@Preview
@Composable
fun SegmentedControlTransparentPreview() {
  PreviewTheme {
    var selected by remember { mutableStateOf(TABS.first()) }
    SegmentedControlContainer(
      modifier = Modifier.fillMaxWidth(),
      configuration = { it.copy(colors = PreviewTransparentSegmentedControlContainerColors) },
    ) {
      TABS.forEach { title ->
        toggle(
          key = title,
          selected = title == selected,
          onClick = { selected = title },
          configuration = { previewTransparentSegmentedControlToggleConfiguration() },
        ) {
          Text(text = title)
        }
      }
    }
  }
}

@ShowkaseComposable(name = "Transparent container — grey deselected, disabled", group = GROUP)
@Preview
@Composable
fun SegmentedControlTransparentDisabledPreview() {
  PreviewTheme {
    SegmentedControlContainer(
      modifier = Modifier.fillMaxWidth(),
      enabled = false,
      configuration = { it.copy(colors = PreviewTransparentSegmentedControlContainerColors) },
    ) {
      TABS.forEachIndexed { index, title ->
        toggle(
          key = title,
          selected = index == 0,
          enabled = false,
          onClick = {},
          configuration = { previewTransparentSegmentedControlToggleConfiguration() },
        ) {
          Text(text = title)
        }
      }
    }
  }
}

@ShowkaseComposable(name = "Toggle — grey deselected", group = GROUP)
@Preview
@Composable
fun SegmentedControlGreyToggleStatesPreview() {
  PreviewTheme {
    var leftSelected by remember { mutableStateOf(true) }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      SegmentedControlToggle(
        selected = leftSelected,
        onClick = { leftSelected = true },
        configuration = { previewTransparentSegmentedControlToggleConfiguration() },
      ) {
        Text(text = if (leftSelected) "Selected" else "Deselected")
      }
      SegmentedControlToggle(
        selected = !leftSelected,
        onClick = { leftSelected = false },
        configuration = { previewTransparentSegmentedControlToggleConfiguration() },
      ) {
        Text(text = if (leftSelected) "Deselected" else "Selected")
      }
    }
  }
}

@ShowkaseComposable(name = "Container — slow animation, radius morph", group = GROUP)
@Preview
@Composable
fun SegmentedControlSlowAnimationPreview() {
  PreviewTheme {
    var selected by remember { mutableStateOf(TABS.first()) }
    SegmentedControlContainer(modifier = Modifier.fillMaxWidth()) {
      TABS.forEach { title ->
        toggle(
          key = title,
          selected = title == selected,
          onClick = { selected = title },
          configuration = {
            it.copy(
              selectedCornerRadius = 20.dp,
              deselectedCornerRadius = 4.dp,
              colorAnimationSpec = tween(durationMillis = 600),
              cornerRadiusAnimationSpec = tween(durationMillis = 600),
            )
          },
        ) {
          Text(text = title)
        }
      }
    }
  }
}
