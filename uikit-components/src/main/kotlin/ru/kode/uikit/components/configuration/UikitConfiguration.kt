package ru.kode.uikit.components.configuration

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import ru.kode.uikit.components.BottomSheetConfiguration
import ru.kode.uikit.components.ButtonColors
import ru.kode.uikit.components.ButtonConfiguration
import ru.kode.uikit.components.CheckboxConfiguration
import ru.kode.uikit.components.RadioButtonConfiguration
import ru.kode.uikit.components.TextFieldConfiguration

@Immutable
data class UikitConfiguration(
  val checkbox: CheckboxConfiguration,
  val radioButton: RadioButtonConfiguration,
  val buttonConfigurations: List<ButtonConfiguration>,
  val buttonColors: List<ButtonColors>,
  val bottomSheetConfiguration: BottomSheetConfiguration,
  val textFieldConfiguration: TextFieldConfiguration,
  val textFieldAreaConfiguration: TextFieldConfiguration,
)

val LocalUikitConfiguration = staticCompositionLocalOf<UikitConfiguration> {
  error("no UikitConfiguration provided")
}
