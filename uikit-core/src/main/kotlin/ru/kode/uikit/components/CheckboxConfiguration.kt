package ru.kode.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

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

/** What the [Checkbox] `box` slot draws from. Interaction, padding and size stay with the Checkbox. */
@Immutable
data class CheckboxSlotState(
  val checked: Boolean,
  val enabled: Boolean,
  val error: Boolean,
  val configuration: CheckboxConfiguration,
)
