package ru.kode.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

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

/** What the [RadioButton] `indicator` slot draws from. Interaction, padding and size stay with the RadioButton. */
@Immutable
data class RadioButtonSlotState(
  val selected: Boolean,
  val enabled: Boolean,
  val error: Boolean,
  val configuration: RadioButtonConfiguration,
)
