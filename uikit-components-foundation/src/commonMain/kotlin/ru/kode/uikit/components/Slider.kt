package ru.kode.uikit.components

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
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
  val state = UikitRangeSliderState(value.start, value.endInclusive, valueRange, steps)
  val trackBounds = remember { TrackBounds() }
  var activeThumb by remember { mutableStateOf(RangeThumb.Start) }

  val gestureModifier = if (enabled) {
    Modifier
      .pointerInput(valueRange, steps) {
        detectTapGestures { offset ->
          val fraction = trackBounds.fraction(offset.x)
          activeThumb = nearestThumb(fraction, state)
          updateRange(fraction, activeThumb, state, onValueChange)
        }
      }
      .pointerInput(valueRange, steps) {
        detectHorizontalDragGestures(
          onDragStart = { offset ->
            val fraction = trackBounds.fraction(offset.x)
            activeThumb = nearestThumb(fraction, state)
            updateRange(fraction, activeThumb, state, onValueChange)
          },
          onHorizontalDrag = { change, _ ->
            updateRange(trackBounds.fraction(change.position.x), activeThumb, state, onValueChange)
          },
        )
      }
  } else {
    Modifier
  }

  SliderLayout(
    modifier = modifier,
    gestures = gestureModifier,
    steps = steps,
    thumbFractions = listOf(state.coercedStartFraction, state.coercedEndFraction),
    trackBounds = trackBounds,
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
  val trackBounds = remember { TrackBounds() }

  val gestureModifier = if (enabled) {
    Modifier
      .pointerInput(valueRange, steps) {
        detectTapGestures { offset ->
          onValueChange(fractionToValue(trackBounds.fraction(offset.x), valueRange, steps))
        }
      }
      .pointerInput(valueRange, steps) {
        detectHorizontalDragGestures { change, _ ->
          onValueChange(fractionToValue(trackBounds.fraction(change.position.x), valueRange, steps))
        }
      }
  } else {
    Modifier
  }

  SliderLayout(
    modifier = modifier,
    gestures = gestureModifier,
    steps = steps,
    thumbFractions = listOf(state.coercedValueAsFraction),
    trackBounds = trackBounds,
    track = { track(state) },
    thumbs = listOf({ thumb(state) }),
  )
}

/** Horizontal extent of the track inside the slider, written on layout and read by gestures. */
private class TrackBounds {
  var start by mutableFloatStateOf(0f)
  var width by mutableFloatStateOf(0f)

  fun fraction(x: Float) = if (width > 0f) ((x - start) / width).coerceIn(0f, 1f) else 0f
}

/**
 * Port of M3 `SliderImpl`/`RangeSliderImpl` layout: the track is inset by half a thumb on each side and the
 * thumbs are placed along it, the whole thing padded to the 48dp minimum touch size.
 */
@Composable
private fun SliderLayout(
  modifier: Modifier,
  gestures: Modifier,
  steps: Int,
  thumbFractions: List<Float>,
  trackBounds: TrackBounds,
  track: @Composable () -> Unit,
  thumbs: List<@Composable () -> Unit>,
) {
  Layout(
    modifier = modifier.minimumTouchSize().then(gestures),
    content = {
      thumbs.forEachIndexed { index, thumb ->
        Box(Modifier.layoutId("$THUMB_LAYOUT_ID$index")) { thumb() }
      }
      Box(Modifier.layoutId(TRACK_LAYOUT_ID)) { track() }
    },
  ) { measurables, constraints ->
    val thumbPlaceables = measurables
      .filter { it.layoutId.toString().startsWith(THUMB_LAYOUT_ID) }
      .map { it.measure(constraints.copy(minWidth = 0)) }
    val startThumbWidth = thumbPlaceables.first().width
    val endThumbWidth = thumbPlaceables.last().width
    val inset = (startThumbWidth + endThumbWidth) / 2
    val trackPlaceable = measurables
      .first { it.layoutId == TRACK_LAYOUT_ID }
      .measure(constraints.offset(horizontal = -inset).copy(minHeight = 0))
    val width = trackPlaceable.width + inset
    val height = maxOf(trackPlaceable.height, thumbPlaceables.maxOf { it.height })
    val trackCornerSize = trackPlaceable.height / 2
    val trackOffsetX = startThumbWidth / 2

    trackBounds.start = trackOffsetX.toFloat()
    trackBounds.width = trackPlaceable.width.toFloat()

    layout(width, height) {
      trackPlaceable.place(trackOffsetX, (height - trackPlaceable.height) / 2)
      thumbPlaceables.forEachIndexed { index, placeable ->
        val fraction = thumbFractions[index]
        // The range end thumb is shifted when thumbs differ in width, as in M3.
        val correction = if (index > 0) (startThumbWidth - endThumbWidth) / 2 else 0
        val x = if (steps > 0 && fraction != 0f && fraction != 1f) {
          ((trackPlaceable.width - trackCornerSize * 2) * fraction + correction).roundToInt() + trackCornerSize
        } else {
          (trackPlaceable.width * fraction + correction).roundToInt()
        }
        placeable.place(x, (height - placeable.height) / 2)
      }
    }
  }
}

// M3 `minimumInteractiveComponentSize`: at least 48dp each way, content centered.
private fun Modifier.minimumTouchSize() = layout { measurable, constraints ->
  val placeable = measurable.measure(constraints)
  val min = MinTouchSize.roundToPx()
  val width = maxOf(placeable.width, min)
  val height = maxOf(placeable.height, min)
  layout(width, height) {
    placeable.place(((width - placeable.width) / 2f).roundToInt(), ((height - placeable.height) / 2f).roundToInt())
  }
}

object Slider {
  @Composable
  fun Thumb(configuration: SliderConfiguration, enabled: Boolean = true) {
    val thumbColor = if (enabled) configuration.thumbColor else configuration.disabledThumbColor
    Box(
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
    Canvas(Modifier.fillMaxWidth().height(configuration.trackHeight)) {
      drawTrack(configuration, state.steps, 0f, state.coercedValueAsFraction, false, activeColor, inactiveColor)
    }
  }

  @Composable
  fun Track(configuration: SliderConfiguration, state: UikitRangeSliderState, enabled: Boolean = true) {
    val (activeColor, inactiveColor) = rangeTrackColors(configuration, enabled)
    Canvas(Modifier.fillMaxWidth().height(configuration.trackHeight)) {
      drawTrack(
        configuration,
        state.steps,
        state.coercedStartFraction,
        state.coercedEndFraction,
        true,
        activeColor,
        inactiveColor,
      )
    }
  }

  enum class Direction {
    Min,
    Max,
  }
}

/**
 * Port of M3 `SliderDefaults.drawTrack` (horizontal, LTR, no corner shrinking): each inactive segment ends in a
 * stop indicator dot of the active color, and segments keep half a thumb plus `thumbTrackGapSize` away from the
 * thumb, with `trackInsideCornerSize` corners facing it.
 */
private fun DrawScope.drawTrack(
  configuration: SliderConfiguration,
  steps: Int,
  activeRangeStart: Float,
  activeRangeEnd: Float,
  isRange: Boolean,
  activeColor: Color,
  inactiveColor: Color,
) {
  // The gap uses the configured thumb width, not the measured one, so a custom thumb slot of another
  // width gets a mismatched gap. Pass measured widths down from SliderLayout if that matters.
  val thumbWidth = configuration.thumbSize.width.roundToPx()
  val gap = if (configuration.thumbTrackGapSize > 0.dp) {
    thumbWidth / 2f + configuration.thumbTrackGapSize.toPx()
  } else {
    0f
  }
  val corner = size.height / 2
  val insideCorner = configuration.trackInsideCornerSize.toPx()
  fun valueX(fraction: Float) = if (steps > 0 && fraction != 0f && fraction != 1f) {
    (size.width - corner * 2) * fraction + corner
  } else {
    size.width * fraction
  }
  val valueStart = valueX(activeRangeStart)
  val valueEnd = valueX(activeRangeEnd)
  val stopRadius = StopIndicatorSize.toPx() / 2

  if (isRange && valueStart > gap + corner) {
    drawTrackPath(0f, valueStart - gap, inactiveColor, corner, insideCorner)
    drawCircle(activeColor, stopRadius, Offset(corner, center.y))
  }
  if (valueEnd < size.width - gap - corner) {
    drawTrackPath(valueEnd + gap, size.width, inactiveColor, insideCorner, corner)
    drawCircle(activeColor, stopRadius, Offset(size.width - corner, center.y))
  }
  val activeStart = if (isRange) valueStart + gap else 0f
  val activeEnd = valueEnd - gap
  val startCorner = if (isRange) insideCorner else corner
  if (activeEnd - activeStart > startCorner) {
    drawTrackPath(activeStart, activeEnd, activeColor, startCorner, insideCorner)
  }
  // No step ticks, SliderConfiguration has no tick colors (the M3 backend passes Unspecified).
}

private fun DrawScope.drawTrackPath(start: Float, end: Float, color: Color, startCorner: Float, endCorner: Float) {
  val startRadius = CornerRadius(startCorner)
  val endRadius = CornerRadius(endCorner)
  val path = Path()
  path.addRoundRect(RoundRect(Rect(start, 0f, end, size.height), startRadius, endRadius, endRadius, startRadius))
  drawPath(path, color)
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
private val MinTouchSize = 48.dp
private val StopIndicatorSize = 4.dp
