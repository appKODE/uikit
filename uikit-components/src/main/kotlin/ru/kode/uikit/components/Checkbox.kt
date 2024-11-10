package ru.kode.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@Composable
fun Checkbox(
  checked: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  onClick: () -> Unit = { },
  configuration: (CheckboxConfiguration) -> CheckboxConfiguration = { it }
) {
  val configurationInternal by rememberUpdatedState(configuration(LocalUikitConfiguration.current.checkbox))
  val colors = remember(configurationInternal, error) {
    CheckboxColors(
      checkedCheckmarkColor = configurationInternal.checkmarkColor,
      uncheckedCheckmarkColor = configurationInternal.checkmarkColor,
      checkedBoxColor = if (error) {
        configurationInternal.errorCheckedBoxColor
      } else {
        configurationInternal.checkedBoxColor
      },
      uncheckedBoxColor = if (error) {
        configurationInternal.errorUncheckedBoxColor
      } else {
        configurationInternal.uncheckedBoxColor
      },
      disabledCheckedBoxColor = configurationInternal.disabledCheckedBoxColor,
      disabledUncheckedBoxColor = configurationInternal.disabledUncheckedBoxColor,
      disabledIndeterminateBoxColor = configurationInternal.disabledCheckedBoxColor,
      checkedBorderColor = if (error) {
        configurationInternal.errorCheckedBorderColor
      } else {
        configurationInternal.checkedBorderColor
      },
      uncheckedBorderColor = if (error) {
        configurationInternal.errorUncheckedBorderColor
      } else {
        configurationInternal.uncheckedBorderColor
      },
      disabledBorderColor = configurationInternal.disabledCheckedBorderColor,
      disabledUncheckedBorderColor = configurationInternal.disabledUncheckedBorderColor,
      disabledIndeterminateBorderColor = configurationInternal.disabledCheckedBorderColor
    )
  }
  androidx.compose.material3.Checkbox(
    checked = checked,
    onCheckedChange = { onClick() },
    modifier = modifier
      .padding(configurationInternal.contentPadding)
      .clip(configurationInternal.shape)
      .size(configurationInternal.size),
    enabled = enabled,
    colors = colors
  )
}

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
