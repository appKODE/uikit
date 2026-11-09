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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

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
  androidx.compose.material3.RangeSlider(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    valueRange = valueRange,
    startThumb = { startThumb(it.toUikit()) },
    endThumb = { endThumb(it.toUikit()) },
    track = { CompositionLocalProvider(LocalMaterialRangeSliderState provides it) { track(it.toUikit()) } },
    steps = steps,
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
    thumb = { thumb(it.toUikit()) },
    track = { CompositionLocalProvider(LocalMaterialSliderState provides it) { track(it.toUikit()) } },
    valueRange = valueRange,
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
  thumb: @Composable (UikitSliderState) -> Unit = {
    val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.sliderConfiguration))
    Slider.Thumb(config, enabled)
  },
  track: @Composable (UikitSliderState) -> Unit = {
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
    thumb = { thumb(it.toUikit()) },
    track = { CompositionLocalProvider(LocalMaterialSliderState provides it) { track(it.toUikit()) } },
    valueRange = valueRange,
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
        shape = CircleShape,
      ),
      interactionSource = remember { MutableInteractionSource() },
      colors = colors,
      enabled = enabled,
      thumbSize = configuration.thumbSize,
    )
  }

  @Composable
  fun Track(
    configuration: SliderConfiguration,
    direction: Direction,
    state: UikitSliderState,
    enabled: Boolean = true,
  ) {
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
      disabledActiveTrackColor,
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
      sliderState = LocalMaterialSliderState.current
        ?: remember(state.value, state.steps, state.valueRange) { state.toMaterial() },
      enabled = enabled,
      colors = colors,
      thumbTrackGapSize = configuration.thumbTrackGapSize,
      trackInsideCornerSize = configuration.trackInsideCornerSize,
    )
  }

  @Composable
  fun Track(configuration: SliderConfiguration, state: UikitRangeSliderState, enabled: Boolean = true) {
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
      rangeSliderState = LocalMaterialRangeSliderState.current
        ?: remember(state.activeRangeStart, state.activeRangeEnd, state.steps, state.valueRange) {
          state.toMaterial()
        },
      enabled = enabled,
      colors = colors,
      thumbTrackGapSize = configuration.thumbTrackGapSize,
      trackInsideCornerSize = configuration.trackInsideCornerSize,
    )
  }

  enum class Direction {
    Min,
    Max,
  }
}

// M3 lays out thumb width/ticks on its own state; a rebuilt state lacks them (no thumb gap),
// so the default Track reads the live one when called inside an M3 slider.
private val LocalMaterialSliderState = compositionLocalOf<SliderState?> { null }
private val LocalMaterialRangeSliderState = compositionLocalOf<RangeSliderState?> { null }

// The public API speaks the backend-neutral core states; M3 states stay an implementation detail.
private fun SliderState.toUikit() = UikitSliderState(value, valueRange, steps)

private fun RangeSliderState.toUikit() = UikitRangeSliderState(activeRangeStart, activeRangeEnd, valueRange, steps)

private fun UikitSliderState.toMaterial() = SliderState(value = value, steps = steps, valueRange = valueRange)

private fun UikitRangeSliderState.toMaterial() = RangeSliderState(
  activeRangeStart = activeRangeStart,
  activeRangeEnd = activeRangeEnd,
  steps = steps,
  valueRange = valueRange,
)
