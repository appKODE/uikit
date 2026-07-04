package ru.kode.uikit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
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
) {
  val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.checkbox))
  val boxColor = when {
    !enabled -> if (checked) config.disabledCheckedBoxColor else config.disabledUncheckedBoxColor
    checked -> if (error) config.errorCheckedBoxColor else config.checkedBoxColor
    else -> if (error) config.errorUncheckedBoxColor else config.uncheckedBoxColor
  }
  val borderColor = when {
    !enabled -> if (checked) config.disabledCheckedBorderColor else config.disabledUncheckedBorderColor
    checked -> if (error) config.errorCheckedBorderColor else config.checkedBorderColor
    else -> if (error) config.errorUncheckedBorderColor else config.uncheckedBorderColor
  }
  val checkmarkColor = config.checkmarkColor
  val shape = config.shape
  val checkFraction by animateFloatAsState(if (checked) 1f else 0f, label = "checkbox-check")

  Canvas(
    modifier = modifier
      .toggleable(
        value = checked,
        onValueChange = { onClick() },
        enabled = enabled,
        role = Role.Checkbox,
        interactionSource = remember { MutableInteractionSource() },
        indication = uikitRipple(bounded = false, color = boxColor, radius = config.size),
      )
      .padding(config.contentPadding)
      .clip(shape)
      .size(config.size),
  ) {
    val outline = shape.createOutline(size, layoutDirection, this)
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
        color = checkmarkColor,
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

private val CheckboxBorderWidth = 2.dp
private val CheckmarkStrokeWidth = 2.dp

@Immutable
data class CheckboxConfiguration(
  val size: Dp,
  val contentPadding: PaddingValues,
  val shape: Shape,
  val checkmarkColor: Color,
  val checkedBoxColor: Color,
  val uncheckedBoxColor: Color,
  val errorCheckedBoxColor: Color,
  val errorUncheckedBoxColor: Color,
  val disabledCheckedBoxColor: Color,
  val disabledUncheckedBoxColor: Color,
  val checkedBorderColor: Color,
  val uncheckedBorderColor: Color,
  val errorCheckedBorderColor: Color,
  val errorUncheckedBorderColor: Color,
  val disabledCheckedBorderColor: Color,
  val disabledUncheckedBorderColor: Color,
)
