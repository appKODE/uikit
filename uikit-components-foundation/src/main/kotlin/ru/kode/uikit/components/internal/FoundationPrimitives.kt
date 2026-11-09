package ru.kode.uikit.components.internal

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Theme-agnostic ripple [IndicationNodeFactory] built on the standalone `material-ripple` module — the
 * same node `androidx.compose.material3.ripple()` uses internally, but without the Material3 dependency.
 * [color] is applied explicitly (no `MaterialTheme` required).
 */
@Stable
internal fun uikitRipple(color: Color, bounded: Boolean = true, radius: Dp = Dp.Unspecified): IndicationNodeFactory =
  UikitRippleNodeFactory(bounded, radius, color)

private class UikitRippleNodeFactory(private val bounded: Boolean, private val radius: Dp, private val color: Color) :
  IndicationNodeFactory {
  override fun create(interactionSource: InteractionSource): DelegatableNode = createRippleModifierNode(
    interactionSource = interactionSource,
    bounded = bounded,
    radius = radius,
    color = ColorProducer { color },
    rippleAlpha = { DefaultRippleAlpha },
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is UikitRippleNodeFactory) return false
    return bounded == other.bounded && radius == other.radius && color == other.color
  }

  override fun hashCode(): Int {
    var result = bounded.hashCode()
    result = 31 * result + radius.hashCode()
    result = 31 * result + color.hashCode()
    return result
  }
}

private val DefaultRippleAlpha = RippleAlpha(
  draggedAlpha = 0.16f,
  focusedAlpha = 0.12f,
  hoveredAlpha = 0.08f,
  pressedAlpha = 0.12f,
)

/**
 * Foundation-only replacement for `androidx.compose.material3.Text`. Renders via [BasicText]; [color],
 * when specified, overrides the color carried by [style].
 */
@Composable
internal fun UikitText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = TextStyle.Default,
  color: Color = Color.Unspecified,
  maxLines: Int = Int.MAX_VALUE,
  overflow: TextOverflow = TextOverflow.Clip,
) {
  BasicText(
    text = text,
    modifier = modifier,
    style = style,
    color = if (color.isSpecified) ColorProducer { color } else null,
    maxLines = maxLines,
    overflow = overflow,
  )
}

/**
 * Foundation-only replacement for `androidx.compose.material3.Icon`. Tints [painter] with [tint] (when
 * specified) and falls back to a 24.dp box only when the painter has no intrinsic size — matching the
 * Material3 behavior.
 */
@Composable
internal fun UikitIcon(
  painter: Painter,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  tint: Color = Color.Unspecified,
) {
  val colorFilter = remember(tint) { if (tint.isSpecified) ColorFilter.tint(tint) else null }
  val semantics = if (contentDescription != null) {
    Modifier.semantics {
      this.contentDescription = contentDescription
      this.role = Role.Image
    }
  } else {
    Modifier
  }
  Box(
    modifier
      .defaultSizeFor(painter)
      .paint(painter, colorFilter = colorFilter, contentScale = ContentScale.Fit)
      .then(semantics),
  )
}

private fun Modifier.defaultSizeFor(painter: Painter) = this.then(
  if (painter.intrinsicSize == Size.Unspecified || painter.intrinsicSize.isInfinite()) {
    DefaultIconSizeModifier
  } else {
    Modifier
  },
)

private fun Size.isInfinite() = width.isInfinite() && height.isInfinite()

private val DefaultIconSizeModifier = Modifier.size(24.dp)

/**
 * Foundation-only replacement for `androidx.compose.material3.CircularProgressIndicator` (indeterminate):
 * a port of the Material 3 1.4 drawing and animation specs. Size comes from [modifier].
 */
@Composable
internal fun UikitCircularLoader(color: Color, strokeWidth: Dp, modifier: Modifier = Modifier) {
  val transition = rememberInfiniteTransition(label = "circular-loader")
  val globalRotation by transition.animateFloat(
    initialValue = 0f,
    targetValue = 1080f,
    animationSpec = infiniteRepeatable(tween(LOADER_DURATION, easing = LinearEasing)),
    label = "global-rotation",
  )
  val additionalRotation by transition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      keyframes {
        durationMillis = LOADER_DURATION
        90f at LOADER_ROTATION_DURATION using EmphasizedDecelerate
        90f at LOADER_ROTATION_DELAY
        180f at LOADER_ROTATION_DURATION + LOADER_ROTATION_DELAY
        180f at LOADER_ROTATION_DELAY * 2
        270f at LOADER_ROTATION_DURATION + LOADER_ROTATION_DELAY * 2
        270f at LOADER_ROTATION_DELAY * 3
        360f at LOADER_ROTATION_DURATION + LOADER_ROTATION_DELAY * 3
        360f at LOADER_DURATION
      },
    ),
    label = "additional-rotation",
  )
  val progress by transition.animateFloat(
    initialValue = LOADER_MIN_PROGRESS,
    targetValue = LOADER_MAX_PROGRESS,
    animationSpec = infiniteRepeatable(
      keyframes {
        durationMillis = LOADER_DURATION
        LOADER_MAX_PROGRESS at LOADER_DURATION / 2 using CubicBezierEasing(0.2f, 0f, 0f, 1f)
        LOADER_MIN_PROGRESS at LOADER_DURATION
      },
    ),
    label = "progress",
  )
  Canvas(modifier.size(40.dp)) {
    val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    val inset = stroke.width / 2
    rotate(globalRotation + additionalRotation) {
      drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = progress * 360f,
        useCenter = false,
        topLeft = Offset(inset, inset),
        size = Size(size.width - stroke.width, size.width - stroke.width),
        style = stroke,
      )
    }
  }
}

private val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
private const val LOADER_DURATION = 6000
private const val LOADER_ROTATION_DELAY = 1500
private const val LOADER_ROTATION_DURATION = 300
private const val LOADER_MIN_PROGRESS = 0.1f
private const val LOADER_MAX_PROGRESS = 0.87f
