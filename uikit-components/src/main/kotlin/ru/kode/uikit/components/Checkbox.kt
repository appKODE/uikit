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
  val checkedColor = remember(configurationInternal, error) {
    if (error) configurationInternal.errorCheckedColor else configurationInternal.checkedColor
  }
  val uncheckedColor = remember(configurationInternal, error) {
    if (error) configurationInternal.errorUncheckedColor else configurationInternal.uncheckedColor
  }
  val colors = remember(configurationInternal, checkedColor, uncheckedColor) {
    CheckboxColors(
      checkedCheckmarkColor = configurationInternal.checkmarkColor,
      uncheckedCheckmarkColor = configurationInternal.checkmarkColor,
      checkedBoxColor = checkedColor,
      uncheckedBoxColor = uncheckedColor,
      disabledCheckedBoxColor = configurationInternal.disabledCheckedColor,
      disabledUncheckedBoxColor = configurationInternal.disabledUncheckedColor,
      disabledIndeterminateBoxColor = configurationInternal.disabledCheckedColor,
      checkedBorderColor = checkedColor,
      uncheckedBorderColor = uncheckedColor,
      disabledBorderColor = configurationInternal.disabledCheckedColor,
      disabledUncheckedBorderColor = configurationInternal.disabledUncheckedColor,
      disabledIndeterminateBorderColor = configurationInternal.disabledCheckedColor
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
  val checkedColor: Color,
  val uncheckedColor: Color,
  val errorCheckedColor: Color,
  val errorUncheckedColor: Color,
  val disabledCheckedColor: Color,
  val disabledUncheckedColor: Color,
)
