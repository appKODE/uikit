package ru.kode.uikit.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import ru.kode.uikit.components.internal.uikitRipple
import kotlin.math.floor
import kotlin.math.max

@Composable
fun Checkbox(
  checked: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  onClick: () -> Unit = { },
  configuration: (CheckboxConfiguration) -> CheckboxConfiguration = { it },
  box: @Composable (CheckboxSlotState) -> Unit = { UikitCheckboxDefaults.Box(it) },
) {
  val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.checkbox))
  val state = CheckboxSlotState(checked = checked, enabled = enabled, error = error, configuration = config)
  Box(
    modifier = modifier
      .toggleable(
        value = checked,
        onValueChange = { onClick() },
        enabled = enabled,
        role = Role.Checkbox,
        interactionSource = remember { MutableInteractionSource() },
        indication = uikitRipple(bounded = false, color = state.boxColor(), radius = config.size),
      )
      .padding(config.contentPadding)
      .clip(config.shape)
      .size(config.size),
    contentAlignment = Alignment.Center,
  ) {
    box(state)
  }
}

object UikitCheckboxDefaults {
  /** Default `box` slot: a port of the Material 3 checkbox drawing with [CheckboxConfiguration] colors. */
  @Composable
  fun Box(state: CheckboxSlotState) {
    val config = state.configuration
    val boxColor = state.boxColor()
    val borderColor = when {
      !state.enabled -> if (state.checked) config.disabledCheckedBorderColor else config.disabledUncheckedBorderColor
      state.checked -> if (state.error) config.errorCheckedBorderColor else config.checkedBorderColor
      else -> if (state.error) config.errorUncheckedBorderColor else config.uncheckedBorderColor
    }
    val checkFraction by updateTransition(state.checked, label = "checkbox").animateFloat(
      transitionSpec = { if (targetState) spring(0.9f, 700f) else snap(delayMillis = 100) },
      label = "checkbox-check",
    ) { if (it) 1f else 0f }
    val strokeWidthPx = with(LocalDensity.current) { floor(CheckboxStrokeWidth.toPx()) }
    val checkPath = remember { Path() }
    val pathToDraw = remember { Path() }
    val pathMeasure = remember { PathMeasure() }
    Canvas(Modifier.padding(2.dp).wrapContentSize(Alignment.Center).requiredSize(20.dp)) {
      val boxSize = size.width
      val radius = 2.dp.toPx()
      if (boxColor == borderColor) {
        drawRoundRect(boxColor, size = Size(boxSize, boxSize), cornerRadius = CornerRadius(radius))
      } else {
        drawRoundRect(
          boxColor,
          topLeft = Offset(strokeWidthPx, strokeWidthPx),
          size = Size(boxSize - strokeWidthPx * 2, boxSize - strokeWidthPx * 2),
          cornerRadius = CornerRadius(max(0f, radius - strokeWidthPx)),
        )
        val half = strokeWidthPx / 2
        drawRoundRect(
          borderColor,
          topLeft = Offset(half, half),
          size = Size(boxSize - strokeWidthPx, boxSize - strokeWidthPx),
          cornerRadius = CornerRadius(radius - half),
          style = Stroke(strokeWidthPx),
        )
      }
      checkPath.rewind()
      checkPath.moveTo(boxSize * 0.2f, boxSize * 0.5f)
      checkPath.lineTo(boxSize * 0.4f, boxSize * 0.7f)
      checkPath.lineTo(boxSize * 0.8f, boxSize * 0.3f)
      pathMeasure.setPath(checkPath, false)
      pathToDraw.rewind()
      pathMeasure.getSegment(0f, pathMeasure.length * checkFraction, pathToDraw, true)
      drawPath(pathToDraw, config.checkmarkColor, style = Stroke(strokeWidthPx, cap = StrokeCap.Square))
    }
  }
}

private fun CheckboxSlotState.boxColor() = when {
  !enabled -> if (checked) configuration.disabledCheckedBoxColor else configuration.disabledUncheckedBoxColor
  checked -> if (error) configuration.errorCheckedBoxColor else configuration.checkedBoxColor
  else -> if (error) configuration.errorUncheckedBoxColor else configuration.uncheckedBoxColor
}

private val CheckboxStrokeWidth = 2.dp
