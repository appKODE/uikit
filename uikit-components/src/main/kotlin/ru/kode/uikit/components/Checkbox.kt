package ru.kode.uikit.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@Composable
fun Checkbox(
  checked: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  onClick: () -> Unit = { },
  configuration: (CheckboxConfiguration) -> CheckboxConfiguration = { it },
  box: @Composable (CheckboxSlotState) -> Unit = { UikitCheckboxDefaults.Box(it) },
) {
  val configurationInternal by rememberUpdatedState(configuration(LocalUikitConfiguration.current.checkbox))
  Box(
    modifier = modifier
      .padding(configurationInternal.contentPadding)
      .clip(configurationInternal.shape)
      .size(configurationInternal.size)
      .toggleable(
        value = checked,
        onValueChange = { onClick() },
        enabled = enabled,
        role = Role.Checkbox,
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = false),
      ),
    contentAlignment = Alignment.Center,
  ) {
    box(CheckboxSlotState(checked = checked, enabled = enabled, error = error, configuration = configurationInternal))
  }
}

object UikitCheckboxDefaults {
  /** Default `box` slot: the Material 3 checkbox drawn with [CheckboxConfiguration] colors. */
  @Composable
  fun Box(state: CheckboxSlotState) {
    val configurationInternal = state.configuration
    val error = state.error
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
        disabledIndeterminateBorderColor = configurationInternal.disabledCheckedBorderColor,
      )
    }
    androidx.compose.material3.Checkbox(
      checked = state.checked,
      onCheckedChange = null,
      enabled = state.enabled,
      colors = colors,
    )
  }
}
