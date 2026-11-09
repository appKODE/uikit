package ru.kode.uikit.components

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@Immutable
class UikitSliderState(val value: Float, val valueRange: ClosedFloatingPointRange<Float>, val steps: Int) {
  val coercedValueAsFraction: Float
    get() = fractionOf(value, valueRange)
}

@Immutable
class UikitRangeSliderState(
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
