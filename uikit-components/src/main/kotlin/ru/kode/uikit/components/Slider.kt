@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kode.uikit.components

import androidx.annotation.IntRange
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@Composable
fun RangeSlider(
  value: ClosedFloatingPointRange<Float>,
  onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  configuration: (SliderConfiguration) -> SliderConfiguration = { it },
  startThumb: @Composable (RangeSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  endThumb: @Composable (RangeSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (RangeSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Track(config, it, enabled)
  },
  @IntRange(from = 0) steps: Int = 0,
) {
  androidx.compose.material3.RangeSlider(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    valueRange = valueRange,
    startThumb = startThumb,
    endThumb = endThumb,
    track = track,
    steps = steps
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
  thumb: @Composable (SliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (SliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Track(config, Slider.Direction.Min, it, enabled)
  },
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
  androidx.compose.material3.Slider(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    steps = steps,
    thumb = thumb,
    track = track,
    valueRange = valueRange
  )
}

@Composable
fun MaxSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  @IntRange(from = 0) steps: Int = 0,
  configuration: (SliderConfiguration) -> SliderConfiguration = { it },
  thumb: @Composable (SliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (SliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Track(config, Slider.Direction.Max, it, enabled)
  },
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
) {
  androidx.compose.material3.Slider(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    steps = steps,
    thumb = thumb,
    track = track,
    valueRange = valueRange
  )
}

object Slider {
  @Composable
  fun Thumb(configuration: SliderConfiguration, enabled: Boolean = true) {
    val colors = remember(configuration) {
      SliderColors(
        thumbColor = configuration.thumbColor,
        activeTrackColor = Color.Unspecified,
        activeTickColor = Color.Unspecified,
        inactiveTrackColor = Color.Unspecified,
        inactiveTickColor = Color.Unspecified,
        disabledThumbColor = configuration.disabledThumbColor,
        disabledActiveTrackColor = Color.Unspecified,
        disabledActiveTickColor = Color.Unspecified,
        disabledInactiveTrackColor = Color.Unspecified,
        disabledInactiveTickColor = Color.Unspecified,
      )
    }
    SliderDefaults.Thumb(
      modifier = Modifier.border(
        width = configuration.thumbBorderWidth,
        color = configuration.thumbBorderColor,
        shape = CircleShape
      ),
      interactionSource = remember { MutableInteractionSource() },
      colors = colors,
      enabled = enabled,
      thumbSize = configuration.thumbSize
    )
  }

  @Composable
  fun Track(configuration: SliderConfiguration, direction: Direction, state: SliderState, enabled: Boolean = true) {
    val inactiveTrackColor = remember(configuration, direction) {
      when (direction) {
        Direction.Min -> configuration.activeTrackColor
        Direction.Max -> configuration.inactiveTrackColor
      }
    }
    val activeTrackColor = remember(configuration, direction) {
      when (direction) {
        Direction.Min -> configuration.inactiveTrackColor
        Direction.Max -> configuration.activeTrackColor
      }
    }
    val disabledInactiveTrackColor = remember(configuration, direction) {
      when (direction) {
        Direction.Min -> configuration.disabledActiveTrackColor
        Direction.Max -> configuration.disabledInactiveTrackColor
      }
    }
    val disabledActiveTrackColor = remember(configuration, direction) {
      when (direction) {
        Direction.Min -> configuration.disabledInactiveTrackColor
        Direction.Max -> configuration.disabledActiveTrackColor
      }
    }
    val colors = remember(
      inactiveTrackColor,
      activeTrackColor,
      disabledInactiveTrackColor,
      disabledActiveTrackColor
    ) {
      SliderColors(
        thumbColor = Color.Unspecified,
        activeTrackColor = activeTrackColor,
        activeTickColor = Color.Unspecified,
        inactiveTrackColor = inactiveTrackColor,
        inactiveTickColor = Color.Unspecified,
        disabledThumbColor = Color.Unspecified,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledActiveTickColor = Color.Unspecified,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
        disabledInactiveTickColor = Color.Unspecified,
      )
    }
    SliderDefaults.Track(
      modifier = Modifier.height(configuration.trackHeight),
      sliderState = state,
      enabled = enabled,
      colors = colors,
      thumbTrackGapSize = configuration.thumbTrackGapSize,
      trackInsideCornerSize = configuration.trackInsideCornerSize
    )
  }

  @Composable
  fun Track(configuration: SliderConfiguration, state: RangeSliderState, enabled: Boolean = true) {
    val colors = remember(configuration) {
      SliderColors(
        thumbColor = Color.Unspecified,
        activeTrackColor = configuration.activeTrackColor,
        activeTickColor = Color.Unspecified,
        inactiveTrackColor = configuration.inactiveTrackColor,
        inactiveTickColor = Color.Unspecified,
        disabledThumbColor = Color.Unspecified,
        disabledActiveTrackColor = configuration.disabledActiveTrackColor,
        disabledActiveTickColor = Color.Unspecified,
        disabledInactiveTrackColor = configuration.disabledActiveTrackColor,
        disabledInactiveTickColor = Color.Unspecified,
      )
    }
    SliderDefaults.Track(
      modifier = Modifier.height(configuration.trackHeight),
      rangeSliderState = state,
      enabled = enabled,
      colors = colors,
      thumbTrackGapSize = configuration.thumbTrackGapSize,
      trackInsideCornerSize = configuration.trackInsideCornerSize
    )
  }

  enum class Direction {
    Min,
    Max
  }
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
