package ru.kode.uikit.components.configuration

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import ru.kode.uikit.components.BottomSheetConfiguration
import ru.kode.uikit.components.ButtonColors
import ru.kode.uikit.components.ButtonConfiguration
import ru.kode.uikit.components.CheckboxConfiguration
import ru.kode.uikit.components.PageIndicatorConfiguration
import ru.kode.uikit.components.RadioButtonConfiguration
import ru.kode.uikit.components.SliderConfiguration
import ru.kode.uikit.components.TextFieldAreaConfiguration
import ru.kode.uikit.components.TextFieldConfiguration
import ru.kode.uikit.components.shimmer.ShimmerConfiguration

@Immutable
data class UikitConfiguration(
  val checkbox: CheckboxConfiguration,
  val radioButton: RadioButtonConfiguration,
  val buttonConfigurations: List<ButtonConfiguration>,
  val buttonColors: List<ButtonColors>,
  val bottomSheetConfiguration: BottomSheetConfiguration,
  val textFieldConfiguration: TextFieldConfiguration,
  val textFieldAreaConfiguration: TextFieldAreaConfiguration,
  val pageIndicatorConfiguration: PageIndicatorConfiguration,
  val sliderConfiguration: SliderConfiguration,
  val shimmerConfiguration: ShimmerConfiguration,
)

val LocalUikitConfiguration = compositionLocalOf<UikitConfiguration> {
  error("no UikitConfiguration provided")
}
