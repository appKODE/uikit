package ru.kode.uikit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
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
  indicator: @Composable (RadioButtonSlotState) -> Unit = { UikitRadioButtonDefaults.Indicator(it) },
) {
  val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.radioButton))
  val state = RadioButtonSlotState(selected = selected, enabled = enabled, error = error, configuration = config)
  val selectableModifier = if (onClick != null) {
    Modifier.selectable(
      selected = selected,
      onClick = onClick,
      enabled = enabled,
      role = Role.RadioButton,
      interactionSource = remember { MutableInteractionSource() },
      indication = uikitRipple(bounded = false, color = state.ringColor(), radius = config.size / 2),
    )
  } else {
    Modifier
  }
  Box(
    modifier = modifier
      .then(selectableModifier)
      .padding(config.contentPadding)
      .size(config.size),
  ) {
    indicator(state)
  }
}

object UikitRadioButtonDefaults {
  /** Default `indicator` slot: a ring with an animated center dot. */
  @Composable
  fun Indicator(state: RadioButtonSlotState) {
    val config = state.configuration
    val ringColor = state.ringColor()
    val dotColor = when {
      !state.enabled -> config.disabledSelectedColor
      state.error -> config.errorSelectedColor
      else -> config.selectedColor
    }
    val dotFraction by animateFloatAsState(if (state.selected) 1f else 0f, label = "radio-dot")
    Canvas(Modifier.fillMaxSize()) {
      val stroke = RadioStrokeWidth.toPx()
      val outerRadius = (size.minDimension - stroke) / 2f
      drawCircle(color = ringColor, radius = outerRadius, style = Stroke(width = stroke))
      if (dotFraction > 0f) {
        drawCircle(color = dotColor, radius = size.minDimension * MAX_DOT_RADIUS_FRACTION * dotFraction)
      }
    }
  }
}

private fun RadioButtonSlotState.ringColor() = when {
  !enabled -> if (selected) configuration.disabledSelectedColor else configuration.disabledUnselectedColor
  selected -> if (error) configuration.errorSelectedColor else configuration.selectedColor
  else -> if (error) configuration.errorUnselectedColor else configuration.unselectedColor
}

private val RadioStrokeWidth = 2.dp
private const val MAX_DOT_RADIUS_FRACTION = 0.25f
