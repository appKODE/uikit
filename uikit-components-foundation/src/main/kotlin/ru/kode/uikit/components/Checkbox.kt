package ru.kode.uikit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import ru.kode.uikit.components.internal.uikitRipple

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
  ) {
    box(state)
  }
}

object UikitCheckboxDefaults {
  /** Default `box` slot: a filled, bordered [CheckboxConfiguration.shape] with an animated checkmark. */
  @Composable
  fun Box(state: CheckboxSlotState) {
    val config = state.configuration
    val boxColor = state.boxColor()
    val borderColor = when {
      !state.enabled -> if (state.checked) config.disabledCheckedBorderColor else config.disabledUncheckedBorderColor
      state.checked -> if (state.error) config.errorCheckedBorderColor else config.checkedBorderColor
      else -> if (state.error) config.errorUncheckedBorderColor else config.uncheckedBorderColor
    }
    val checkFraction by animateFloatAsState(if (state.checked) 1f else 0f, label = "checkbox-check")
    Canvas(Modifier.fillMaxSize()) {
      val outline = config.shape.createOutline(size, layoutDirection, this)
      val boxPath = Path().apply {
        when (outline) {
          is Outline.Rectangle -> addRect(outline.rect)
          is Outline.Rounded -> addRoundRect(outline.roundRect)
          is Outline.Generic -> addPath(outline.path)
        }
      }
      drawPath(boxPath, color = boxColor)
      drawPath(boxPath, color = borderColor, style = Stroke(width = CheckboxBorderWidth.toPx()))
      if (checkFraction > 0f) {
        val w = size.width
        val h = size.height
        val checkPath = Path().apply {
          moveTo(w * 0.22f, h * 0.52f)
          lineTo(w * 0.42f, h * 0.72f)
          lineTo(w * 0.78f, h * 0.30f)
        }
        drawPath(
          path = checkPath,
          color = config.checkmarkColor,
          alpha = checkFraction,
          style = Stroke(
            width = CheckmarkStrokeWidth.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
          ),
        )
      }
    }
  }
}

private fun CheckboxSlotState.boxColor() = when {
  !enabled -> if (checked) configuration.disabledCheckedBoxColor else configuration.disabledUncheckedBoxColor
  checked -> if (error) configuration.errorCheckedBoxColor else configuration.checkedBoxColor
  else -> if (error) configuration.errorUncheckedBoxColor else configuration.uncheckedBoxColor
}

private val CheckboxBorderWidth = 2.dp
private val CheckmarkStrokeWidth = 2.dp
