package ru.kode.uikit.components.configuration

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import ru.kode.uikit.components.BottomSheetConfiguration
import ru.kode.uikit.components.ButtonColors
import ru.kode.uikit.components.ButtonConfiguration
import ru.kode.uikit.components.CheckboxConfiguration
import ru.kode.uikit.components.DatePickerConfiguration
import ru.kode.uikit.components.PageIndicatorConfiguration
import ru.kode.uikit.components.RadioButtonConfiguration
import ru.kode.uikit.components.SliderConfiguration
import ru.kode.uikit.components.TextFieldAreaConfiguration
import ru.kode.uikit.components.TextFieldConfiguration
import ru.kode.uikit.components.shimmer.ShimmerConfiguration

@Immutable
data class UikitConfiguration(
  val checkbox: CheckboxConfiguration? = null,
  val radioButton: RadioButtonConfiguration? = null,
  val buttonConfigurations: List<ButtonConfiguration> = emptyList(),
  val buttonColors: List<ButtonColors> = emptyList(),
  val bottomSheetConfiguration: BottomSheetConfiguration? = null,
  val textFieldConfiguration: TextFieldConfiguration? = null,
  val textFieldAreaConfiguration: TextFieldAreaConfiguration? = null,
  val pageIndicatorConfiguration: PageIndicatorConfiguration? = null,
  val sliderConfiguration: SliderConfiguration? = null,
  val shimmerConfiguration: ShimmerConfiguration? = null,
  val datePickerConfiguration: DatePickerConfiguration? = null,
)

val LocalUikitConfiguration = compositionLocalOf<UikitConfiguration> {
  error("no UikitConfiguration provided")
}
