package ru.kode.uikit.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
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
    contentAlignment = Alignment.Center,
  ) {
    indicator(state)
  }
}

object UikitRadioButtonDefaults {
  /** Default `indicator` slot: a port of the Material 3 radio button drawing with [RadioButtonConfiguration] colors. */
  @Composable
  fun Indicator(state: RadioButtonSlotState) {
    val ringColor = state.ringColor()
    val dotRadius by animateDpAsState(
      targetValue = if (state.selected) RadioDotSize / 2 else 0.dp,
      animationSpec = spring(0.9f, 1400f),
      label = "radio-dot",
    )
    Canvas(Modifier.wrapContentSize(Alignment.Center).padding(2.dp).requiredSize(RadioIconSize)) {
      val strokeWidth = RadioStrokeWidth.toPx()
      drawCircle(ringColor, radius = (RadioIconSize / 2).toPx() - strokeWidth / 2, style = Stroke(strokeWidth))
      if (dotRadius > 0.dp) {
        drawCircle(ringColor, dotRadius.toPx() - strokeWidth / 2)
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
private val RadioIconSize = 20.dp
private val RadioDotSize = 12.dp
