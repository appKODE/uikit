@file:OptIn(ExperimentalMaterial3Api::class)

package ru.kode.uikit.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TextFieldDatePicker(
  state: DatePickerState,
  onDateConfirm: (LocalDate) -> Unit,
  modifier: Modifier = Modifier,
  label: String? = null,
  helper: String? = null,
  enabled: Boolean = true,
  error: Boolean = false,
  configuration: (DatePickerConfiguration) -> DatePickerConfiguration = { it },
) {
  val configurationInternal by rememberUpdatedState(
    configuration(LocalUikitConfiguration.current.datePickerConfiguration)
  )
  var isModalDatePickerActive by remember { mutableStateOf(false) }
  val textColor = remember(enabled, error, configurationInternal.textField) {
    when {
      !enabled -> configurationInternal.textField.disabledTextColor
      error -> configurationInternal.textField.errorTextColor
      else -> configurationInternal.textField.textColor
    }
  }
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    if (label != null) {
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = label,
        style = configurationInternal.textField.labelTextStyle,
        color = remember(enabled, error, configurationInternal.textField) {
          when {
            !enabled -> configurationInternal.textField.disabledLabelColor
            error -> configurationInternal.textField.errorLabelColor
            else -> configurationInternal.textField.labelColor
          }
        }
      )
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .border(
          width = 1.dp,
          color = remember(enabled, error, configurationInternal.textField) {
            when {
              !enabled -> configurationInternal.textField.disabledBorderColor
              error -> configurationInternal.textField.errorBorderColor
              else -> configurationInternal.textField.borderColor
            }
          },
          shape = configurationInternal.textField.shape
        )
        .background(
          color = remember(enabled, error, configurationInternal.textField) {
            when {
              !enabled -> configurationInternal.textField.disabledBackgroundColor
              error -> configurationInternal.textField.errorBackgroundColor
              else -> configurationInternal.textField.backgroundColor
            }
          },
          shape = configurationInternal.textField.shape
        )
        .clip(configurationInternal.textField.shape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(
            color = textColor
          ),
          enabled = enabled,
          onClick = { isModalDatePickerActive = true }
        )
        .padding(configurationInternal.textField.contentPadding),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      configurationInternal.textField.leadingIconRes?.let {
        Icon(
          painter = painterResource(it),
          contentDescription = null,
          tint = remember(enabled, configurationInternal.textField) {
            if (enabled) {
              configurationInternal.textField.leadingIconColor
            } else {
              configurationInternal.textField.disabledLeadingIconColor
            }
          }
        )
      }
      val formattedDate = remember(state.selectedDate) {
        state.selectedDate.format(DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_TIME_PATTERN))
      }
      Text(
        modifier = Modifier.weight(1f),
        text = formattedDate,
        style = configurationInternal.textField.textStyle,
        color = textColor
      )
      Icon(
        painter = painterResource(configurationInternal.textField.trailingIconRes),
        contentDescription = null,
        tint = remember(enabled, configurationInternal.textField) {
          if (enabled) {
            configurationInternal.textField.trailingIconColor
          } else {
            configurationInternal.textField.disabledTrailingIconColor
          }
        }
      )
      if (error) {
        Icon(
          painter = painterResource(configurationInternal.textField.errorIconRes),
          contentDescription = null,
          tint = configurationInternal.textField.errorIconColor
        )
      }
    }
    if (helper != null) {
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = helper,
        style = configurationInternal.textField.helperTextStyle,
        color = remember(enabled, error, configurationInternal.textField) {
          when {
            !enabled -> configurationInternal.textField.disabledHelperColor
            error -> configurationInternal.textField.errorHelperColor
            else -> configurationInternal.textField.helperColor
          }
        }
      )
    }
  }
  if (isModalDatePickerActive) {
    ModalDatePicker(
      date = state.selectedDate,
      configuration = configurationInternal.modal,
      onDismissRequest = { isModalDatePickerActive = false },
      onDateConfirm = {
        state.selectedDate = it
        onDateConfirm(it)
        isModalDatePickerActive = false
      },
      yearRange = state.yearRange,
      selectableDates = state.selectableDates
    )
  }
}

@Composable
private fun InlineDatePicker(
  state: DatePickerState,
  onDateConfirm: (LocalDate) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  configuration: (DatePickerConfiguration) -> DatePickerConfiguration = { it },
) {
  val configurationInternal by rememberUpdatedState(
    configuration(LocalUikitConfiguration.current.datePickerConfiguration)
  )
  var isModalDatePickerActive by remember { mutableStateOf(false) }
  val textColor = remember(enabled, configurationInternal.inline) {
    if (enabled) {
      configurationInternal.inline.textColor
    } else {
      configurationInternal.inline.disabledTextColor
    }
  }
  Row(
    modifier = modifier.clickable(
      interactionSource = remember { MutableInteractionSource() },
      indication = ripple(
        color = textColor
      ),
      enabled = enabled,
      onClick = { isModalDatePickerActive = true }
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    val formattedDate = remember(state.selectedDate) {
      state.selectedDate.format(DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_TIME_PATTERN))
    }
    Text(
      text = formattedDate,
      style = configurationInternal.inline.textStyle,
      color = textColor
    )
    Icon(
      modifier = Modifier.size(24.dp),
      painter = painterResource(configurationInternal.inline.iconRes),
      contentDescription = null,
      tint = remember(enabled, configurationInternal.inline) {
        if (enabled) {
          configurationInternal.inline.iconColor
        } else {
          configurationInternal.inline.disabledIconColor
        }
      }
    )
  }
  if (isModalDatePickerActive) {
    ModalDatePicker(
      date = state.selectedDate,
      configuration = configurationInternal.modal,
      onDismissRequest = { isModalDatePickerActive = false },
      onDateConfirm = {
        state.selectedDate = it
        onDateConfirm(it)
        isModalDatePickerActive = false
      },
      yearRange = state.yearRange,
      selectableDates = state.selectableDates
    )
  }
}

@Composable
private fun ModalDatePicker(
  date: LocalDate,
  configuration: ModalDatePickerConfiguration,
  onDismissRequest: () -> Unit,
  onDateConfirm: (LocalDate) -> Unit,
  yearRange: IntRange = DatePickerDefaults.YearRange,
  selectableDates: SelectableDates = DatePickerDefaults.AllDates,
) {
  val state = androidx.compose.material3.rememberDatePickerState(
    initialSelectedDateMillis = date.atStartOfDay(defaultZoneId).toInstant().toEpochMilli(),
    yearRange = yearRange,
    selectableDates = selectableDates
  )
  var isUserSelection by remember { mutableStateOf(false) }
  LaunchedEffect(state.selectedDateMillis) {
    if (isUserSelection) {
      state.selectedDateMillis?.toLocalDate()?.let { onDateConfirm(it) }
    } else {
      isUserSelection = true
    }
  }
  BasicAlertDialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    androidx.compose.material3.DatePicker(
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(16.dp)),
      state = state,
      title = {
        Text(
          modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
          text = stringResource(configuration.titleRes),
          style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = TextUnit(0.004f, TextUnitType.Em)
          ),
          color = Color(0xFF000000).copy(alpha = 0.7f)
        )
      },
      headline = {
        val formattedDate by remember {
          derivedStateOf {
            (state.selectedDateMillis?.toLocalDate() ?: date)
              .format(DateTimeFormatter.ofPattern(MODAL_HEADLINE_TIME_PATTERN))
          }
        }
        Text(
          modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
          text = formattedDate,
          style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
          ),
          color = Color(0xFF000000)
        )
      },
      showModeToggle = false,
      colors = rememberModalDatePickerColors(configuration),
    )
  }
}

@Composable
fun rememberDatePickerState(
  initialSelectedDate: LocalDate = LocalDate.now(),
  yearRange: IntRange = DatePickerDefaults.YearRange,
  selectableDates: SelectableDates = DatePickerDefaults.AllDates,
): DatePickerState {
  return remember {
    DatePickerState(
      selectedDate = initialSelectedDate,
      yearRange = yearRange,
      selectableDates = selectableDates
    )
  }
}

@Stable
data class DatePickerState(
  var selectedDate: LocalDate,
  val yearRange: IntRange,
  val selectableDates: SelectableDates,
)

@Immutable
data class DatePickerConfiguration(
  val textField: TextFieldDatePickerConfiguration,
  val inline: InlineDatePickerConfiguration,
  val modal: ModalDatePickerConfiguration,
)

@Immutable
data class TextFieldDatePickerConfiguration(
  @DrawableRes val leadingIconRes: Int?,
  @DrawableRes val trailingIconRes: Int,
  @DrawableRes val errorIconRes: Int,
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
  @DrawableRes val iconRes: Int,
  val textStyle: TextStyle,
  val textColor: Color,
  val disabledTextColor: Color,
  val iconColor: Color,
  val disabledIconColor: Color,
)

@Immutable
data class ModalDatePickerConfiguration(
  @StringRes val titleRes: Int,
  @StringRes val confirmButtonTextRes: Int,
  @StringRes val cancelButtonTextRes: Int,
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

@Composable
private fun rememberModalDatePickerColors(configuration: ModalDatePickerConfiguration): DatePickerColors {
  val stubDateTextFieldColors = DatePickerDefaults.colors().dateTextFieldColors
  return remember(configuration) {
    DatePickerColors(
      containerColor = configuration.containerColor,
      titleContentColor = configuration.titleContentColor,
      headlineContentColor = configuration.headlineContentColor,
      weekdayContentColor = configuration.weekdayContentColor,
      subheadContentColor = configuration.subheadContentColor,
      navigationContentColor = configuration.navigationContentColor,
      yearContentColor = configuration.yearContentColor,
      disabledYearContentColor = configuration.disabledYearContentColor,
      currentYearContentColor = configuration.currentYearContentColor,
      selectedYearContentColor = configuration.selectedYearContentColor,
      disabledSelectedYearContentColor = configuration.disabledSelectedYearContentColor,
      selectedYearContainerColor = configuration.selectedYearContainerColor,
      disabledSelectedYearContainerColor = configuration.disabledSelectedYearContainerColor,
      dayContentColor = configuration.dayContentColor,
      disabledDayContentColor = configuration.disabledDayContentColor,
      selectedDayContentColor = configuration.selectedDayContentColor,
      disabledSelectedDayContentColor = configuration.disabledSelectedDayContentColor,
      selectedDayContainerColor = configuration.selectedDayContainerColor,
      disabledSelectedDayContainerColor = configuration.disabledSelectedDayContainerColor,
      todayContentColor = configuration.todayContentColor,
      todayDateBorderColor = configuration.todayDateBorderColor,
      dayInSelectionRangeContentColor = configuration.dayInSelectionRangeContentColor,
      dayInSelectionRangeContainerColor = configuration.dayInSelectionRangeContainerColor,
      dividerColor = configuration.dividerColor,
      dateTextFieldColors = stubDateTextFieldColors,
    )
  }
}

private fun Long.toLocalDate() = Instant
  .ofEpochMilli(this)
  .atZone(defaultZoneId)
  .toLocalDate()

private val defaultZoneId = ZoneId.of("UTC")
private const val DAY_MONTH_YEAR_TIME_PATTERN = "dd.MM.yyyy"
private const val MODAL_HEADLINE_TIME_PATTERN = "MMM d, yyyy"
