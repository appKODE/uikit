package ru.kode.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@Composable
fun RadioButton(
  selected: Boolean,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  configuration: (RadioButtonConfiguration) -> RadioButtonConfiguration = { it }
) {
  val configurationInternal by rememberUpdatedState(configuration(LocalUikitConfiguration.current.radioButton))
  val colors = remember(configurationInternal, error) {
    RadioButtonColors(
      selectedColor = if (error) {
        configurationInternal.errorSelectedColor
      } else {
        configurationInternal.selectedColor
      },
      unselectedColor = if (error) {
        configurationInternal.errorUnselectedColor
      } else {
        configurationInternal.unselectedColor
      },
      disabledSelectedColor = configurationInternal.disabledSelectedColor,
      disabledUnselectedColor = configurationInternal.disabledUnselectedColor
    )
  }
  androidx.compose.material3.RadioButton(
    selected = selected,
    onClick = onClick,
    modifier = modifier
      .padding(configurationInternal.contentPadding)
      .size(configurationInternal.size),
    enabled = enabled,
    colors = colors
  )
}

@Immutable
data class RadioButtonConfiguration(
  val size: Dp,
  val contentPadding: PaddingValues,
  val selectedColor: Color,
  val unselectedColor: Color,
  val errorSelectedColor: Color,
  val errorUnselectedColor: Color,
  val disabledSelectedColor: Color,
  val disabledUnselectedColor: Color,
)
