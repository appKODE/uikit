package ru.kode.uikit.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@Composable
fun RadioButton(
  selected: Boolean,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  configuration: (RadioButtonConfiguration) -> RadioButtonConfiguration = { it },
  indicator: @Composable (RadioButtonSlotState) -> Unit = { UikitRadioButtonDefaults.Indicator(it) },
) {
  val configurationInternal by rememberUpdatedState(configuration(LocalUikitConfiguration.current.radioButton))
  val selectableModifier = if (onClick != null) {
    Modifier.selectable(
      selected = selected,
      onClick = onClick,
      enabled = enabled,
      role = Role.RadioButton,
      interactionSource = remember { MutableInteractionSource() },
      indication = ripple(bounded = false),
    )
  } else {
    Modifier
  }
  Box(
    modifier = modifier
      .padding(configurationInternal.contentPadding)
      .size(configurationInternal.size)
      .then(selectableModifier),
    contentAlignment = Alignment.Center,
  ) {
    indicator(
      RadioButtonSlotState(
        selected = selected,
        enabled = enabled,
        error = error,
        configuration = configurationInternal,
      ),
    )
  }
}

object UikitRadioButtonDefaults {
  /** Default `indicator` slot: the Material 3 radio button drawn with [RadioButtonConfiguration] colors. */
  @Composable
  fun Indicator(state: RadioButtonSlotState) {
    val configurationInternal = state.configuration
    val error = state.error
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
        disabledUnselectedColor = configurationInternal.disabledUnselectedColor,
      )
    }
    androidx.compose.material3.RadioButton(
      selected = state.selected,
      onClick = null,
      enabled = state.enabled,
      colors = colors,
    )
  }
}
