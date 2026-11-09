package ru.kode.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun rememberDatePickerState(
  initialSelectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
  yearRange: IntRange = UikitDatePickerDefaults.YearRange,
  selectableDates: SelectableDates = UikitDatePickerDefaults.AllDates,
): DatePickerState = remember {
  DatePickerState(
    selectedDate = initialSelectedDate,
    yearRange = yearRange,
    selectableDates = selectableDates,
  )
}

/**
 * Determines which dates and years are selectable in the date picker dialog. Backend-neutral replacement
 * for `androidx.compose.material3.SelectableDates`.
 */
interface SelectableDates {
  fun isSelectableDate(utcTimeMillis: Long): Boolean = true

  fun isSelectableYear(year: Int): Boolean = true
}

object UikitDatePickerDefaults {
  val YearRange: IntRange = 1900..2100

  val AllDates: SelectableDates = object : SelectableDates {}
}

@Stable
data class DatePickerState(var selectedDate: LocalDate, val yearRange: IntRange, val selectableDates: SelectableDates)

@Immutable
data class DatePickerConfiguration(
  val textField: TextFieldDatePickerConfiguration,
  val inline: InlineDatePickerConfiguration,
  val modal: ModalDatePickerConfiguration,
)

@Immutable
data class TextFieldDatePickerConfiguration(
  val leadingIcon: (@Composable () -> Painter)?,
  val trailingIcon: @Composable () -> Painter,
  val errorIcon: @Composable () -> Painter,
  val shape: Shape,
  val contentPadding: PaddingValues,
  val textStyle: TextStyle,
  val labelTextStyle: TextStyle,
  val helperTextStyle: TextStyle,
  val backgroundColor: Color,
  val borderColor: Color,
  val textColor: Color,
  val labelColor: Color,
  val helperColor: Color,
  val leadingIconColor: Color,
  val trailingIconColor: Color,
  val errorBackgroundColor: Color,
  val errorBorderColor: Color,
  val errorTextColor: Color,
  val errorLabelColor: Color,
  val errorHelperColor: Color,
  val errorIconColor: Color,
  val disabledBackgroundColor: Color,
  val disabledBorderColor: Color,
  val disabledTextColor: Color,
  val disabledLabelColor: Color,
  val disabledHelperColor: Color,
  val disabledLeadingIconColor: Color,
  val disabledTrailingIconColor: Color,
)

@Immutable
data class InlineDatePickerConfiguration(
  val icon: @Composable () -> Painter,
  val textStyle: TextStyle,
  val textColor: Color,
  val disabledTextColor: Color,
  val iconColor: Color,
  val disabledIconColor: Color,
)

@Immutable
data class ModalDatePickerConfiguration(
  val title: @Composable () -> String,
  val confirmButtonText: @Composable () -> String,
  val cancelButtonText: @Composable () -> String,
  val bottomButtonsTextStyle: TextStyle,
  val containerColor: Color,
  val titleContentColor: Color,
  val headlineContentColor: Color,
  val weekdayContentColor: Color,
  val subheadContentColor: Color,
  val navigationContentColor: Color,
  val yearContentColor: Color,
  val disabledYearContentColor: Color,
  val currentYearContentColor: Color,
  val selectedYearContentColor: Color,
  val disabledSelectedYearContentColor: Color,
  val selectedYearContainerColor: Color,
  val disabledSelectedYearContainerColor: Color,
  val dayContentColor: Color,
  val disabledDayContentColor: Color,
  val selectedDayContentColor: Color,
  val disabledSelectedDayContentColor: Color,
  val selectedDayContainerColor: Color,
  val disabledSelectedDayContainerColor: Color,
  val todayContentColor: Color,
  val todayDateBorderColor: Color,
  val dayInSelectionRangeContentColor: Color,
  val dayInSelectionRangeContainerColor: Color,
  val dividerColor: Color,
  val confirmButtonColor: Color,
  val cancelButtonColor: Color,
)
