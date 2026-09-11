package ru.kode.uikit.sample

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kode.uikit.components.BottomSheetConfiguration
import ru.kode.uikit.components.ButtonColors
import ru.kode.uikit.components.ButtonConfiguration
import ru.kode.uikit.components.CheckboxConfiguration
import ru.kode.uikit.components.DatePickerConfiguration
import ru.kode.uikit.components.InlineDatePickerConfiguration
import ru.kode.uikit.components.ModalDatePickerConfiguration
import ru.kode.uikit.components.PageIndicatorConfiguration
import ru.kode.uikit.components.RadioButtonConfiguration
import ru.kode.uikit.components.SegmentedControlContainerColors
import ru.kode.uikit.components.SegmentedControlContainerConfiguration
import ru.kode.uikit.components.SegmentedControlToggleColors
import ru.kode.uikit.components.SegmentedControlToggleConfiguration
import ru.kode.uikit.components.SliderConfiguration
import ru.kode.uikit.components.TextFieldAreaConfiguration
import ru.kode.uikit.components.TextFieldConfiguration
import ru.kode.uikit.components.TextFieldDatePickerConfiguration
import ru.kode.uikit.components.configuration.UikitConfiguration
import ru.kode.uikit.components.shimmer.ShimmerConfiguration

/**
 * A concrete [UikitConfiguration] used purely to render the component gallery. The published
 * library ships no default theme, so every component needs a configuration provided via
 * `LocalUikitConfiguration` before it can render. Values here are illustrative, not canonical.
 */
object PreviewTokens {
  val primary = Color(0xFF3F51B5)
  val primaryPressed = Color(0xFF303F9F)
  val onPrimary = Color(0xFFFFFFFF)
  val surface = Color(0xFFFFFFFF)
  val onSurface = Color(0xFF1A1A1A)
  val onSurfaceVariant = Color(0xFF6B6B6B)
  val outline = Color(0xFFBDBDBD)
  val track = Color(0xFFE0E0E0)
  val error = Color(0xFFD32F2F)
  val errorContainer = Color(0xFFFDECEA)
  val disabled = Color(0xFF9E9E9E)
  val disabledContainer = Color(0xFFF0F0F0)
  val scrim = Color(0x66000000)

  val fieldShape: Shape = RoundedCornerShape(12.dp)
  val bodyStyle = TextStyle(fontSize = 16.sp, lineHeight = 20.sp)
  val labelStyle = TextStyle(fontSize = 13.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium)
  val helperStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp)
}

fun previewUikitConfiguration(): UikitConfiguration {
  val t = PreviewTokens
  return UikitConfiguration(
    checkbox = previewCheckboxConfiguration(),
    radioButton = previewRadioButtonConfiguration(),
    buttonConfigurations = listOf(PreviewButtonConfiguration),
    buttonColors = listOf(PreviewPrimaryButtonColors),
    bottomSheetConfiguration = previewBottomSheetConfiguration(),
    textFieldConfiguration = previewTextFieldConfiguration(),
    textFieldAreaConfiguration = previewTextFieldAreaConfiguration(),
    pageIndicatorConfiguration = previewPageIndicatorConfiguration(),
    sliderConfiguration = previewSliderConfiguration(),
    shimmerConfiguration = ShimmerConfiguration(
      shape = RoundedCornerShape(8.dp),
      color = t.track,
      highlightColor = Color(0xFFF5F5F5),
    ),
    datePickerConfiguration = previewDatePickerConfiguration(),
    segmentedControlContainerConfiguration = previewSegmentedControlContainerConfiguration(),
    segmentedControlToggleConfiguration = previewSegmentedControlToggleConfiguration(),
  )
}

// ---- Button (interfaces, implemented as objects) ----

object PreviewButtonConfiguration : ButtonConfiguration {
  override val textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
  override val contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
  override val shape: Shape = RoundedCornerShape(12.dp)
  override val iconSize = 24.dp
  override val contentSpacing = 8.dp
  override val loaderStrokeWidth = 3.dp
}

object PreviewPrimaryButtonColors : ButtonColors {
  override val backgroundColor = PreviewTokens.primary
  override val textColor = PreviewTokens.onPrimary
  override val iconColor = PreviewTokens.onPrimary
  override val loadingBackgroundColor = PreviewTokens.primaryPressed
  override val loadingTextColor = PreviewTokens.onPrimary
  override val loadingIconColor = PreviewTokens.onPrimary
  override val disabledBackgroundColor = PreviewTokens.disabledContainer
  override val disabledTextColor = PreviewTokens.disabled
  override val disabledIconColor = PreviewTokens.disabled
}

private fun previewCheckboxConfiguration() = CheckboxConfiguration(
  size = 24.dp,
  contentPadding = PaddingValues(0.dp),
  shape = RoundedCornerShape(6.dp),
  checkmarkColor = PreviewTokens.onPrimary,
  checkedBoxColor = PreviewTokens.primary,
  uncheckedBoxColor = PreviewTokens.surface,
  errorCheckedBoxColor = PreviewTokens.error,
  errorUncheckedBoxColor = PreviewTokens.surface,
  disabledCheckedBoxColor = PreviewTokens.disabledContainer,
  disabledUncheckedBoxColor = PreviewTokens.disabledContainer,
  checkedBorderColor = PreviewTokens.primary,
  uncheckedBorderColor = PreviewTokens.outline,
  errorCheckedBorderColor = PreviewTokens.error,
  errorUncheckedBorderColor = PreviewTokens.error,
  disabledCheckedBorderColor = PreviewTokens.disabled,
  disabledUncheckedBorderColor = PreviewTokens.disabled,
)

private fun previewRadioButtonConfiguration() = RadioButtonConfiguration(
  size = 24.dp,
  contentPadding = PaddingValues(0.dp),
  selectedColor = PreviewTokens.primary,
  unselectedColor = PreviewTokens.outline,
  errorSelectedColor = PreviewTokens.error,
  errorUnselectedColor = PreviewTokens.error,
  disabledSelectedColor = PreviewTokens.disabled,
  disabledUnselectedColor = PreviewTokens.disabled,
)

private fun previewBottomSheetConfiguration() = BottomSheetConfiguration(
  backgroundColor = PreviewTokens.surface,
  scrimColor = PreviewTokens.scrim,
  sheetMaxWidth = 640.dp,
  cornerRadius = 20.dp,
  handleConfiguration = BottomSheetConfiguration.HandleConfiguration(
    color = PreviewTokens.outline,
    width = 32.dp,
    height = 4.dp,
    verticalPadding = 12.dp,
    shape = CircleShape,
  ),
)

private fun previewTextFieldConfiguration() = TextFieldConfiguration(
  textFieldStyle = PreviewTokens.bodyStyle,
  shape = PreviewTokens.fieldShape,
  borderWidth = 1.dp,
  errorBorderWidth = 2.dp,
  focusedTextColor = PreviewTokens.onSurface,
  unfocusedTextColor = PreviewTokens.onSurface,
  disabledTextColor = PreviewTokens.disabled,
  errorTextColor = PreviewTokens.error,
  focusedContainerColor = PreviewTokens.surface,
  unfocusedContainerColor = PreviewTokens.surface,
  disabledContainerColor = PreviewTokens.disabledContainer,
  errorContainerColor = PreviewTokens.errorContainer,
  focusedBorderColor = PreviewTokens.primary,
  unfocusedBorderColor = PreviewTokens.outline,
  disabledBorderColor = PreviewTokens.disabled,
  errorBorderColor = PreviewTokens.error,
  cursorColor = PreviewTokens.primary,
  errorCursorColor = PreviewTokens.error,
)

private fun previewTextFieldAreaConfiguration() = TextFieldAreaConfiguration(
  textFieldStyle = PreviewTokens.bodyStyle,
  shape = PreviewTokens.fieldShape,
  borderWidth = 1.dp,
  errorBorderWidth = 2.dp,
  focusedTextColor = PreviewTokens.onSurface,
  unfocusedTextColor = PreviewTokens.onSurface,
  disabledTextColor = PreviewTokens.disabled,
  errorTextColor = PreviewTokens.error,
  focusedContainerColor = PreviewTokens.surface,
  unfocusedContainerColor = PreviewTokens.surface,
  disabledContainerColor = PreviewTokens.disabledContainer,
  errorContainerColor = PreviewTokens.errorContainer,
  focusedBorderColor = PreviewTokens.primary,
  unfocusedBorderColor = PreviewTokens.outline,
  disabledBorderColor = PreviewTokens.disabled,
  errorBorderColor = PreviewTokens.error,
  cursorColor = PreviewTokens.primary,
  errorCursorColor = PreviewTokens.error,
)

private fun previewPageIndicatorConfiguration() = PageIndicatorConfiguration(
  size = 8.dp,
  spacing = 8.dp,
  padding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
  activeColor = PreviewTokens.primary,
  inactiveColor = PreviewTokens.outline,
  shape = CircleShape,
  backgroundColor = Color.Transparent,
)

private fun previewSliderConfiguration() = SliderConfiguration(
  thumbSize = DpSize(20.dp, 20.dp),
  thumbBorderWidth = 2.dp,
  thumbTrackGapSize = 4.dp,
  trackHeight = 4.dp,
  trackShape = CircleShape,
  trackInsideCornerSize = 2.dp,
  thumbColor = PreviewTokens.primary,
  thumbBorderColor = PreviewTokens.onPrimary,
  activeTrackColor = PreviewTokens.primary,
  inactiveTrackColor = PreviewTokens.track,
  disabledThumbColor = PreviewTokens.disabled,
  disabledThumbBorderColor = PreviewTokens.onPrimary,
  disabledActiveTrackColor = PreviewTokens.disabled,
  disabledInactiveTrackColor = PreviewTokens.disabledContainer,
)

private fun previewDatePickerConfiguration() = DatePickerConfiguration(
  textField = TextFieldDatePickerConfiguration(
    leadingIconRes = android.R.drawable.ic_menu_my_calendar,
    trailingIconRes = android.R.drawable.arrow_down_float,
    errorIconRes = android.R.drawable.ic_dialog_alert,
    shape = PreviewTokens.fieldShape,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    textStyle = PreviewTokens.bodyStyle,
    labelTextStyle = PreviewTokens.labelStyle,
    helperTextStyle = PreviewTokens.helperStyle,
    backgroundColor = PreviewTokens.surface,
    borderColor = PreviewTokens.outline,
    textColor = PreviewTokens.onSurface,
    labelColor = PreviewTokens.onSurfaceVariant,
    helperColor = PreviewTokens.onSurfaceVariant,
    leadingIconColor = PreviewTokens.primary,
    trailingIconColor = PreviewTokens.onSurfaceVariant,
    errorBackgroundColor = PreviewTokens.errorContainer,
    errorBorderColor = PreviewTokens.error,
    errorTextColor = PreviewTokens.error,
    errorLabelColor = PreviewTokens.error,
    errorHelperColor = PreviewTokens.error,
    errorIconColor = PreviewTokens.error,
    disabledBackgroundColor = PreviewTokens.disabledContainer,
    disabledBorderColor = PreviewTokens.disabled,
    disabledTextColor = PreviewTokens.disabled,
    disabledLabelColor = PreviewTokens.disabled,
    disabledHelperColor = PreviewTokens.disabled,
    disabledLeadingIconColor = PreviewTokens.disabled,
    disabledTrailingIconColor = PreviewTokens.disabled,
  ),
  inline = InlineDatePickerConfiguration(
    iconRes = android.R.drawable.ic_menu_my_calendar,
    textStyle = PreviewTokens.bodyStyle,
    textColor = PreviewTokens.onSurface,
    disabledTextColor = PreviewTokens.disabled,
    iconColor = PreviewTokens.primary,
    disabledIconColor = PreviewTokens.disabled,
  ),
  modal = ModalDatePickerConfiguration(
    titleRes = android.R.string.untitled,
    confirmButtonTextRes = android.R.string.ok,
    cancelButtonTextRes = android.R.string.cancel,
    bottomButtonsTextStyle = PreviewTokens.labelStyle,
    containerColor = PreviewTokens.surface,
    titleContentColor = PreviewTokens.onSurfaceVariant,
    headlineContentColor = PreviewTokens.onSurface,
    weekdayContentColor = PreviewTokens.onSurfaceVariant,
    subheadContentColor = PreviewTokens.onSurfaceVariant,
    navigationContentColor = PreviewTokens.onSurface,
    yearContentColor = PreviewTokens.onSurface,
    disabledYearContentColor = PreviewTokens.disabled,
    currentYearContentColor = PreviewTokens.primary,
    selectedYearContentColor = PreviewTokens.onPrimary,
    disabledSelectedYearContentColor = PreviewTokens.disabled,
    selectedYearContainerColor = PreviewTokens.primary,
    disabledSelectedYearContainerColor = PreviewTokens.disabledContainer,
    dayContentColor = PreviewTokens.onSurface,
    disabledDayContentColor = PreviewTokens.disabled,
    selectedDayContentColor = PreviewTokens.onPrimary,
    disabledSelectedDayContentColor = PreviewTokens.disabled,
    selectedDayContainerColor = PreviewTokens.primary,
    disabledSelectedDayContainerColor = PreviewTokens.disabledContainer,
    todayContentColor = PreviewTokens.primary,
    todayDateBorderColor = PreviewTokens.primary,
    dayInSelectionRangeContentColor = PreviewTokens.onSurface,
    dayInSelectionRangeContainerColor = PreviewTokens.track,
    dividerColor = PreviewTokens.outline,
    confirmButtonColor = PreviewTokens.primary,
    cancelButtonColor = PreviewTokens.onSurfaceVariant,
  ),
)

private fun previewSegmentedControlContainerConfiguration() = SegmentedControlContainerConfiguration(
  cornerRadius = 9.dp,
  colors = PreviewSegmentedControlContainerColors,
  toggleSpacing = 2.dp,
  colorAnimationSpec = tween(durationMillis = 200),
)

private object PreviewSegmentedControlContainerColors : SegmentedControlContainerColors {
  override val backgroundColor = PreviewTokens.track
  override val disabledBackgroundColor = PreviewTokens.disabledContainer
}

private fun previewSegmentedControlToggleConfiguration() = SegmentedControlToggleConfiguration(
  selectedCornerRadius = 10.dp,
  deselectedCornerRadius = 10.dp,
  contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
  borderWidth = 1.dp,
  colors = PreviewSegmentedControlToggleColors,
  colorAnimationSpec = tween(durationMillis = 200),
  cornerRadiusAnimationSpec = tween(durationMillis = 200),
)

private object PreviewSegmentedControlToggleColors : SegmentedControlToggleColors {
  override val selectedBackgroundColor = PreviewTokens.surface
  override val selectedContentColor = PreviewTokens.onSurface
  override val selectedBorderColor = PreviewTokens.primary
  override val deselectedBackgroundColor = Color.Transparent
  override val deselectedContentColor = PreviewTokens.onSurfaceVariant
  override val deselectedBorderColor = Color.Transparent
  override val disabledSelectedBackgroundColor = PreviewTokens.surface
  override val disabledSelectedContentColor = PreviewTokens.disabled
  override val disabledSelectedBorderColor = PreviewTokens.disabled
  override val disabledUnselectedBackgroundColor = Color.Transparent
  override val disabledUnselectedContentColor = PreviewTokens.disabled
  override val disabledUnselectedBorderColor = Color.Transparent
}

/**
 * Alternative palette used by the "transparent" previews: the container draws no track at all and
 * deselected toggles carry the grey, so segments stay readable on a bare surface.
 */
object PreviewTransparentSegmentedControlContainerColors : SegmentedControlContainerColors {
  override val backgroundColor = Color.Transparent
  override val disabledBackgroundColor = Color.Transparent
}

fun previewTransparentSegmentedControlToggleConfiguration() = SegmentedControlToggleConfiguration(
  selectedCornerRadius = 22.dp,
  deselectedCornerRadius = 8.dp,
  contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
  borderWidth = 1.dp,
  colors = PreviewGreySegmentedControlToggleColors,
  colorAnimationSpec = tween(durationMillis = 200),
  cornerRadiusAnimationSpec = tween(durationMillis = 200),
)

object PreviewGreySegmentedControlToggleColors : SegmentedControlToggleColors {
  override val selectedBackgroundColor = PreviewTokens.primary
  override val selectedContentColor = PreviewTokens.onPrimary
  override val selectedBorderColor = PreviewTokens.primary
  override val deselectedBackgroundColor = PreviewTokens.track
  override val deselectedContentColor = PreviewTokens.onSurfaceVariant
  override val deselectedBorderColor = PreviewTokens.outline
  override val disabledSelectedBackgroundColor = PreviewTokens.disabledContainer
  override val disabledSelectedContentColor = PreviewTokens.disabled
  override val disabledSelectedBorderColor = PreviewTokens.disabled
  override val disabledUnselectedBackgroundColor = PreviewTokens.disabledContainer
  override val disabledUnselectedContentColor = PreviewTokens.disabled
  override val disabledUnselectedBorderColor = PreviewTokens.disabled
}
