package ru.kode.uikit.components

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import ru.kode.uikit.components.Slider.Direction
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun RangeSlider(
  value: ClosedFloatingPointRange<Float>,
  onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  configuration: (SliderConfiguration) -> SliderConfiguration = { it },
  startThumb: @Composable (UikitRangeSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  endThumb: @Composable (UikitRangeSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (UikitRangeSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Track(config, it, enabled)
  },
  @IntRange(from = 0) steps: Int = 0,
) {
  val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
  val state = UikitRangeSliderState(value.start, value.endInclusive, valueRange, steps)
  var widthPx by remember { mutableFloatStateOf(0f) }
  var activeThumb by remember { mutableStateOf(RangeThumb.Start) }

  val gestureModifier = if (enabled) {
    Modifier
      .pointerInput(widthPx, valueRange, steps) {
        detectTapGestures { offset ->
          val fraction = (offset.x / widthPx).coerceIn(0f, 1f)
          activeThumb = nearestThumb(fraction, state)
          updateRange(fraction, activeThumb, state, onValueChange)
        }
      }
      .pointerInput(widthPx, valueRange, steps) {
        detectHorizontalDragGestures(
          onDragStart = { offset ->
            val fraction = (offset.x / widthPx).coerceIn(0f, 1f)
            activeThumb = nearestThumb(fraction, state)
            updateRange(fraction, activeThumb, state, onValueChange)
          },
          onHorizontalDrag = { change, _ ->
            val fraction = (change.position.x / widthPx).coerceIn(0f, 1f)
            updateRange(fraction, activeThumb, state, onValueChange)
          },
        )
      }
  } else {
    Modifier
  }

  SliderLayout(
    modifier = modifier.then(gestureModifier),
    thumbFractions = listOf(state.coercedStartFraction, state.coercedEndFraction),
    onWidth = { widthPx = it },
    track = { track(state) },
    thumbs = listOf({ startThumb(state) }, { endThumb(state) }),
  )
}

@Composable
fun MinSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  @IntRange(from = 0) steps: Int = 0,
  configuration: (SliderConfiguration) -> SliderConfiguration = { it },
  thumb: @Composable (UikitSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (UikitSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Track(config, Direction.Min, it, enabled)
  },
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
  SingleSlider(value, onValueChange, modifier, enabled, steps, valueRange, thumb, track)
}

@Composable
fun MaxSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  @IntRange(from = 0) steps: Int = 0,
  configuration: (SliderConfiguration) -> SliderConfiguration = { it },
  thumb: @Composable (UikitSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (UikitSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Track(config, Direction.Max, it, enabled)
  },
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
  SingleSlider(value, onValueChange, modifier, enabled, steps, valueRange, thumb, track)
}

@Composable
private fun SingleSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier,
  enabled: Boolean,
  steps: Int,
  valueRange: ClosedFloatingPointRange<Float>,
  thumb: @Composable (UikitSliderState) -> Unit,
  track: @Composable (UikitSliderState) -> Unit,
) {
  val state = UikitSliderState(value, valueRange, steps)
  var widthPx by remember { mutableFloatStateOf(0f) }

  val gestureModifier = if (enabled) {
    Modifier
      .pointerInput(widthPx, valueRange, steps) {
        detectTapGestures { offset ->
          onValueChange(fractionToValue((offset.x / widthPx).coerceIn(0f, 1f), valueRange, steps))
        }
      }
      .pointerInput(widthPx, valueRange, steps) {
        detectHorizontalDragGestures { change, _ ->
          onValueChange(fractionToValue((change.position.x / widthPx).coerceIn(0f, 1f), valueRange, steps))
        }
      }
  } else {
    Modifier
  }

  SliderLayout(
    modifier = modifier.then(gestureModifier),
    thumbFractions = listOf(state.coercedValueAsFraction),
    onWidth = { widthPx = it },
    track = { track(state) },
    thumbs = listOf({ thumb(state) }),
  )
}

/**
 * Lays out a full-width [track] plus one or more [thumbs], each positioned so its center follows the
 * corresponding entry in [thumbFractions] (0f..1f). Reports the measured width via [onWidth].
 */
@Composable
private fun SliderLayout(
  modifier: Modifier,
  thumbFractions: List<Float>,
  onWidth: (Float) -> Unit,
  track: @Composable () -> Unit,
  thumbs: List<@Composable () -> Unit>,
) {
  Layout(
    modifier = modifier,
    content = {
      androidx.compose.foundation.layout.Box(Modifier.layoutId(TRACK_LAYOUT_ID)) { track() }
      thumbs.forEachIndexed { index, thumb ->
        androidx.compose.foundation.layout.Box(Modifier.layoutId("$THUMB_LAYOUT_ID$index")) { thumb() }
      }
    },
  ) { measurables, constraints ->
    val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
    val thumbPlaceables = measurables
      .filter { it.layoutId.toString().startsWith(THUMB_LAYOUT_ID) }
      .map { it.measure(looseConstraints) }
    val maxThumbWidth = thumbPlaceables.maxOfOrNull { it.width } ?: 0
    val maxThumbHeight = thumbPlaceables.maxOfOrNull { it.height } ?: 0

    val width = if (constraints.hasBoundedWidth) constraints.maxWidth else maxThumbWidth
    val trackPlaceable = measurables
      .first { it.layoutId == TRACK_LAYOUT_ID }
      .measure(looseConstraints.copy(minWidth = width, maxWidth = width))
    val height = maxOf(maxThumbHeight, trackPlaceable.height)

    onWidth(width.toFloat())

    layout(width, height) {
      trackPlaceable.place(0, (height - trackPlaceable.height) / 2)
      thumbPlaceables.forEachIndexed { index, placeable ->
        val fraction = thumbFractions.getOrElse(index) { 0f }
        val x = (fraction * width - placeable.width / 2f).roundToInt().coerceIn(0, width - placeable.width)
        placeable.place(x, (height - placeable.height) / 2)
      }
    }
  }
}

object Slider {
  @Composable
  fun Thumb(configuration: SliderConfiguration, enabled: Boolean = true) {
    val thumbColor = if (enabled) configuration.thumbColor else configuration.disabledThumbColor
    androidx.compose.foundation.layout.Box(
      Modifier
        .size(configuration.thumbSize)
        .border(
          width = configuration.thumbBorderWidth,
          color = configuration.thumbBorderColor,
          shape = CircleShape,
        )
        .background(thumbColor, CircleShape),
    )
  }

  @Composable
  fun Track(
    configuration: SliderConfiguration,
    direction: Direction,
    state: UikitSliderState,
    enabled: Boolean = true,
  ) {
    val (activeColor, inactiveColor) = singleTrackColors(configuration, direction, enabled)
    val fraction = state.coercedValueAsFraction
    Canvas(
      Modifier
        .fillMaxWidth()
        .height(configuration.trackHeight),
    ) {
      val gap = configuration.thumbTrackGapSize.toPx()
      val radius = CornerRadius(size.height / 2f)
      val activeEnd = size.width * fraction
      drawSegment(0f, activeEnd - gap / 2f, activeColor, radius)
      drawSegment(activeEnd + gap / 2f, size.width, inactiveColor, radius)
    }
  }

  @Composable
  fun Track(configuration: SliderConfiguration, state: UikitRangeSliderState, enabled: Boolean = true) {
    val (activeColor, inactiveColor) = rangeTrackColors(configuration, enabled)
    val startFraction = state.coercedStartFraction
    val endFraction = state.coercedEndFraction
    Canvas(
      Modifier
        .fillMaxWidth()
        .height(configuration.trackHeight),
    ) {
      val gap = configuration.thumbTrackGapSize.toPx()
      val radius = CornerRadius(size.height / 2f)
      val startX = size.width * startFraction
      val endX = size.width * endFraction
      drawSegment(0f, startX - gap / 2f, inactiveColor, radius)
      drawSegment(startX + gap / 2f, endX - gap / 2f, activeColor, radius)
      drawSegment(endX + gap / 2f, size.width, inactiveColor, radius)
    }
  }

  enum class Direction {
    Min,
    Max,
  }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSegment(
  startX: Float,
  endX: Float,
  color: Color,
  radius: CornerRadius,
) {
  if (endX <= startX) return
  drawRoundRect(
    color = color,
    topLeft = Offset(startX, 0f),
    size = Size(endX - startX, size.height),
    cornerRadius = radius,
  )
}

private fun singleTrackColors(
  configuration: SliderConfiguration,
  direction: Direction,
  enabled: Boolean,
): Pair<Color, Color> = when {
  !enabled && direction == Direction.Min ->
    configuration.disabledInactiveTrackColor to configuration.disabledActiveTrackColor

  !enabled ->
    configuration.disabledActiveTrackColor to configuration.disabledInactiveTrackColor

  direction == Direction.Min ->
    configuration.inactiveTrackColor to configuration.activeTrackColor

  else ->
    configuration.activeTrackColor to configuration.inactiveTrackColor
}

private fun rangeTrackColors(configuration: SliderConfiguration, enabled: Boolean): Pair<Color, Color> = if (enabled) {
  configuration.activeTrackColor to configuration.inactiveTrackColor
} else {
  configuration.disabledActiveTrackColor to configuration.disabledActiveTrackColor
}

private fun fractionToValue(fraction: Float, valueRange: ClosedFloatingPointRange<Float>, steps: Int): Float {
  val span = valueRange.endInclusive - valueRange.start
  val raw = valueRange.start + fraction * span
  if (steps <= 0 || span == 0f) return raw
  val stepSize = span / (steps + 1)
  return valueRange.start + (((raw - valueRange.start) / stepSize).roundToInt()) * stepSize
}

private fun nearestThumb(fraction: Float, state: UikitRangeSliderState): RangeThumb =
  if (abs(fraction - state.coercedStartFraction) <= abs(fraction - state.coercedEndFraction)) {
    RangeThumb.Start
  } else {
    RangeThumb.End
  }

private fun updateRange(
  fraction: Float,
  thumb: RangeThumb,
  state: UikitRangeSliderState,
  onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
  val newValue = fractionToValue(fraction, state.valueRange, state.steps)
  val range = when (thumb) {
    RangeThumb.Start -> newValue.coerceAtMost(state.activeRangeEnd)..state.activeRangeEnd
    RangeThumb.End -> state.activeRangeStart..newValue.coerceAtLeast(state.activeRangeStart)
  }
  onValueChange(range)
}

private enum class RangeThumb {
  Start,
  End,
}

private const val TRACK_LAYOUT_ID = "track"
private const val THUMB_LAYOUT_ID = "thumb"

@Immutable
class UikitSliderState internal constructor(
  val value: Float,
  val valueRange: ClosedFloatingPointRange<Float>,
  val steps: Int,
) {
  val coercedValueAsFraction: Float
    get() = fractionOf(value, valueRange)
}

@Immutable
class UikitRangeSliderState internal constructor(
  val activeRangeStart: Float,
  val activeRangeEnd: Float,
  val valueRange: ClosedFloatingPointRange<Float>,
  val steps: Int,
) {
  val coercedStartFraction: Float
    get() = fractionOf(activeRangeStart, valueRange)
  val coercedEndFraction: Float
    get() = fractionOf(activeRangeEnd, valueRange)
}

private fun fractionOf(value: Float, valueRange: ClosedFloatingPointRange<Float>): Float {
  val span = valueRange.endInclusive - valueRange.start
  return if (span == 0f) 0f else ((value - valueRange.start) / span).coerceIn(0f, 1f)
}

@Immutable
data class SliderConfiguration(
  val thumbSize: DpSize,
  val thumbBorderWidth: Dp,
  val thumbTrackGapSize: Dp,
  val trackHeight: Dp,
  val trackShape: Shape,
  val trackInsideCornerSize: Dp,
  val thumbColor: Color,
  val thumbBorderColor: Color,
  val activeTrackColor: Color,
  val inactiveTrackColor: Color,
  val disabledThumbColor: Color,
  val disabledThumbBorderColor: Color,
  val disabledActiveTrackColor: Color,
  val disabledInactiveTrackColor: Color,
)
