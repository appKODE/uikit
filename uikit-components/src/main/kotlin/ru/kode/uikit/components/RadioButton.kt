package ru.kode.uikit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import ru.kode.uikit.components.internal.uikitRipple

@Composable
fun RadioButton(
  selected: Boolean,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  configuration: (RadioButtonConfiguration) -> RadioButtonConfiguration = { it },
) {
  val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.radioButton))
  val ringColor = when {
    !enabled -> if (selected) config.disabledSelectedColor else config.disabledUnselectedColor
    selected -> if (error) config.errorSelectedColor else config.selectedColor
    else -> if (error) config.errorUnselectedColor else config.unselectedColor
  }
  val dotColor = when {
    !enabled -> config.disabledSelectedColor
    error -> config.errorSelectedColor
    else -> config.selectedColor
  }
  val dotFraction by animateFloatAsState(if (selected) 1f else 0f, label = "radio-dot")

  val selectableModifier = if (onClick != null) {
    Modifier.selectable(
      selected = selected,
      onClick = onClick,
      enabled = enabled,
      role = Role.RadioButton,
      interactionSource = remember { MutableInteractionSource() },
      indication = uikitRipple(bounded = false, color = ringColor, radius = config.size / 2),
    )
  } else {
    Modifier
  }

  Canvas(
    modifier = modifier
      .then(selectableModifier)
      .padding(config.contentPadding)
      .size(config.size),
  ) {
    val stroke = RadioStrokeWidth.toPx()
    val outerRadius = (size.minDimension - stroke) / 2f
    drawCircle(color = ringColor, radius = outerRadius, style = Stroke(width = stroke))
    if (dotFraction > 0f) {
      drawCircle(color = dotColor, radius = size.minDimension * MAX_DOT_RADIUS_FRACTION * dotFraction)
    }
  }
}

private val RadioStrokeWidth = 2.dp
private const val MAX_DOT_RADIUS_FRACTION = 0.25f

@Immutable
data class RadioButtonConfiguration(
  val size: Dp,
  val contentPadding: PaddingValues,
  val selectedColor: Color,
  val unselectedColor: Color,
  val errorSelectedColor: Color,
  val errorUnselectedColor: Color,
  val disabledSelectedColor: Color,
  val disabledUnselectedColor: Color,
)
