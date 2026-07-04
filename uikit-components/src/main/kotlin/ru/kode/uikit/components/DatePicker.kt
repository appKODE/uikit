package ru.kode.uikit.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import ru.kode.uikit.components.internal.UikitIcon
import ru.kode.uikit.components.internal.UikitText
import ru.kode.uikit.components.internal.uikitRipple
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle as JavaTextStyle

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
    configuration(LocalUikitConfiguration.current.datePickerConfiguration),
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
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    if (label != null) {
      UikitText(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = label,
        style = configurationInternal.textField.labelTextStyle,
        color = remember(enabled, error, configurationInternal.textField) {
          when {
            !enabled -> configurationInternal.textField.disabledLabelColor
            error -> configurationInternal.textField.errorLabelColor
            else -> configurationInternal.textField.labelColor
          }
        },
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
          shape = configurationInternal.textField.shape,
        )
        .background(
          color = remember(enabled, error, configurationInternal.textField) {
            when {
              !enabled -> configurationInternal.textField.disabledBackgroundColor
              error -> configurationInternal.textField.errorBackgroundColor
              else -> configurationInternal.textField.backgroundColor
            }
          },
          shape = configurationInternal.textField.shape,
        )
        .clip(configurationInternal.textField.shape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = uikitRipple(color = textColor),
          enabled = enabled,
          onClick = { isModalDatePickerActive = true },
        )
        .padding(configurationInternal.textField.contentPadding),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      configurationInternal.textField.leadingIconRes?.let {
        UikitIcon(
          painter = painterResource(it),
          contentDescription = null,
          tint = remember(enabled, configurationInternal.textField) {
            if (enabled) {
              configurationInternal.textField.leadingIconColor
            } else {
              configurationInternal.textField.disabledLeadingIconColor
            }
          },
        )
      }
      val formattedDate = remember(state.selectedDate) {
        state.selectedDate.format(DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_TIME_PATTERN))
      }
      UikitText(
        modifier = Modifier.weight(1f),
        text = formattedDate,
        style = configurationInternal.textField.textStyle,
        color = textColor,
      )
      UikitIcon(
        painter = painterResource(configurationInternal.textField.trailingIconRes),
        contentDescription = null,
        tint = remember(enabled, configurationInternal.textField) {
          if (enabled) {
            configurationInternal.textField.trailingIconColor
          } else {
            configurationInternal.textField.disabledTrailingIconColor
          }
        },
      )
      if (error) {
        UikitIcon(
          painter = painterResource(configurationInternal.textField.errorIconRes),
          contentDescription = null,
          tint = configurationInternal.textField.errorIconColor,
        )
      }
    }
    if (helper != null) {
      UikitText(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = helper,
        style = configurationInternal.textField.helperTextStyle,
        color = remember(enabled, error, configurationInternal.textField) {
          when {
            !enabled -> configurationInternal.textField.disabledHelperColor
            error -> configurationInternal.textField.errorHelperColor
            else -> configurationInternal.textField.helperColor
          }
        },
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
      selectableDates = state.selectableDates,
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
    configuration(LocalUikitConfiguration.current.datePickerConfiguration),
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
      indication = uikitRipple(color = textColor),
      enabled = enabled,
      onClick = { isModalDatePickerActive = true },
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    val formattedDate = remember(state.selectedDate) {
      state.selectedDate.format(DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_TIME_PATTERN))
    }
    UikitText(
      text = formattedDate,
      style = configurationInternal.inline.textStyle,
      color = textColor,
    )
    UikitIcon(
      modifier = Modifier.size(24.dp),
      painter = painterResource(configurationInternal.inline.iconRes),
      contentDescription = null,
      tint = remember(enabled, configurationInternal.inline) {
        if (enabled) {
          configurationInternal.inline.iconColor
        } else {
          configurationInternal.inline.disabledIconColor
        }
      },
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
      selectableDates = state.selectableDates,
    )
  }
}

@Composable
private fun ModalDatePicker(
  date: LocalDate,
  configuration: ModalDatePickerConfiguration,
  onDismissRequest: () -> Unit,
  onDateConfirm: (LocalDate) -> Unit,
  yearRange: IntRange = UikitDatePickerDefaults.YearRange,
  selectableDates: SelectableDates = UikitDatePickerDefaults.AllDates,
) {
  var displayedMonth by remember { mutableStateOf(YearMonth.from(date)) }
  var showYearPicker by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Column(
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .widthIn(max = 360.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(configuration.containerColor)
        .padding(bottom = 12.dp),
    ) {
      UikitText(
        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
        text = stringResource(configuration.titleRes),
        style = TitleTextStyle,
        color = configuration.titleContentColor,
      )
      UikitText(
        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 4.dp, bottom = 12.dp),
        text = date.format(DateTimeFormatter.ofPattern(MODAL_HEADLINE_TIME_PATTERN, Locale.getDefault())),
        style = HeadlineTextStyle,
        color = configuration.headlineContentColor,
      )
      Box(
        Modifier
          .fillMaxWidth()
          .height(1.dp)
          .background(configuration.dividerColor),
      )
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        UikitText(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = uikitRipple(bounded = true, color = configuration.subheadContentColor),
            ) { showYearPicker = !showYearPicker }
            .padding(horizontal = 8.dp, vertical = 8.dp),
          text = "${displayedMonth.month.getDisplayName(
            JavaTextStyle.FULL,
            Locale.getDefault(),
          )} ${displayedMonth.year}",
          style = SubheadTextStyle,
          color = configuration.subheadContentColor,
        )
        Box(Modifier.weight(1f))
        if (!showYearPicker) {
          NavButton(
            symbol = "‹",
            color = configuration.navigationContentColor,
            enabled = displayedMonth.year > yearRange.first || displayedMonth.monthValue > 1,
          ) { displayedMonth = displayedMonth.minusMonths(1) }
          NavButton(
            symbol = "›",
            color = configuration.navigationContentColor,
            enabled = displayedMonth.year < yearRange.last || displayedMonth.monthValue < 12,
          ) { displayedMonth = displayedMonth.plusMonths(1) }
        }
      }
      if (showYearPicker) {
        YearPicker(
          yearRange = yearRange,
          selectedYear = displayedMonth.year,
          selectableDates = selectableDates,
          configuration = configuration,
          onYearSelected = {
            displayedMonth = displayedMonth.withYear(it)
            showYearPicker = false
          },
        )
      } else {
        WeekdayHeader(color = configuration.weekdayContentColor)
        MonthGrid(
          displayedMonth = displayedMonth,
          selectedDate = date,
          selectableDates = selectableDates,
          configuration = configuration,
          onDaySelected = onDateConfirm,
        )
      }
    }
  }
}

@Composable
private fun NavButton(symbol: String, color: Color, enabled: Boolean, onClick: () -> Unit) {
  UikitText(
    modifier = Modifier
      .size(40.dp)
      .clip(CircleShape)
      .clickable(
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = uikitRipple(bounded = false, color = color),
        onClick = onClick,
      )
      .padding(8.dp),
    text = symbol,
    style = NavTextStyle,
    color = if (enabled) color else color.copy(alpha = 0.38f),
  )
}

@Composable
private fun WeekdayHeader(color: Color) {
  Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
    WeekDays.forEach { dayOfWeek ->
      UikitText(
        modifier = Modifier.weight(1f).padding(vertical = 4.dp),
        text = dayOfWeek.getDisplayName(JavaTextStyle.SHORT, Locale.getDefault()),
        style = WeekdayTextStyle.copy(textAlign = TextAlign.Center),
        color = color,
      )
    }
  }
}

@Composable
private fun MonthGrid(
  displayedMonth: YearMonth,
  selectedDate: LocalDate,
  selectableDates: SelectableDates,
  configuration: ModalDatePickerConfiguration,
  onDaySelected: (LocalDate) -> Unit,
) {
  val leadingBlanks = (displayedMonth.atDay(1).dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
  val daysInMonth = displayedMonth.lengthOfMonth()
  val cells = leadingBlanks + daysInMonth
  val rows = (cells + 6) / 7
  val today = LocalDate.now()
  Column(modifier = Modifier.padding(horizontal = 12.dp)) {
    for (row in 0 until rows) {
      Row(modifier = Modifier.fillMaxWidth()) {
        for (column in 0 until 7) {
          val cellIndex = row * 7 + column
          val dayOfMonth = cellIndex - leadingBlanks + 1
          Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            if (dayOfMonth in 1..daysInMonth) {
              val cellDate = displayedMonth.atDay(dayOfMonth)
              DayCell(
                date = cellDate,
                selected = cellDate == selectedDate,
                isToday = cellDate == today,
                selectable = selectableDates.isSelectableYear(cellDate.year) &&
                  selectableDates.isSelectableDate(cellDate.toUtcMillis()),
                configuration = configuration,
                onClick = { onDaySelected(cellDate) },
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun DayCell(
  date: LocalDate,
  selected: Boolean,
  isToday: Boolean,
  selectable: Boolean,
  configuration: ModalDatePickerConfiguration,
  onClick: () -> Unit,
) {
  val contentColor = when {
    !selectable -> configuration.disabledDayContentColor
    selected -> configuration.selectedDayContentColor
    isToday -> configuration.todayContentColor
    else -> configuration.dayContentColor
  }
  Box(
    modifier = Modifier
      .padding(2.dp)
      .size(40.dp)
      .clip(CircleShape)
      .background(if (selected) configuration.selectedDayContainerColor else Color.Transparent)
      .then(
        if (isToday && !selected) {
          Modifier.border(1.dp, configuration.todayDateBorderColor, CircleShape)
        } else {
          Modifier
        },
      )
      .clickable(
        enabled = selectable,
        interactionSource = remember { MutableInteractionSource() },
        indication = uikitRipple(bounded = false, color = contentColor),
        onClick = onClick,
      ),
    contentAlignment = Alignment.Center,
  ) {
    UikitText(text = date.dayOfMonth.toString(), style = DayTextStyle, color = contentColor)
  }
}

@Composable
private fun YearPicker(
  yearRange: IntRange,
  selectedYear: Int,
  selectableDates: SelectableDates,
  configuration: ModalDatePickerConfiguration,
  onYearSelected: (Int) -> Unit,
) {
  val years = remember(yearRange) { yearRange.toList() }
  LazyVerticalGrid(
    columns = GridCells.Fixed(3),
    modifier = Modifier
      .fillMaxWidth()
      .heightIn(max = 280.dp)
      .padding(horizontal = 12.dp),
  ) {
    items(years) { year ->
      val selectable = selectableDates.isSelectableYear(year)
      val selected = year == selectedYear
      val contentColor = when {
        !selectable -> configuration.disabledYearContentColor
        selected -> configuration.selectedYearContentColor
        year == LocalDate.now().year -> configuration.currentYearContentColor
        else -> configuration.yearContentColor
      }
      Box(
        modifier = Modifier
          .padding(4.dp)
          .height(36.dp)
          .clip(CircleShape)
          .background(if (selected) configuration.selectedYearContainerColor else Color.Transparent)
          .clickable(
            enabled = selectable,
            interactionSource = remember { MutableInteractionSource() },
            indication = uikitRipple(bounded = true, color = contentColor),
            onClick = { onYearSelected(year) },
          ),
        contentAlignment = Alignment.Center,
      ) {
        UikitText(text = year.toString(), style = DayTextStyle, color = contentColor)
      }
    }
  }
}

@Composable
fun rememberDatePickerState(
  initialSelectedDate: LocalDate = LocalDate.now(),
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
 * Determines which dates and years are selectable in the [ModalDatePicker]. Foundation-only replacement
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

private fun LocalDate.toUtcMillis(): Long = atStartOfDay(defaultZoneId).toInstant().toEpochMilli()

private val WeekDays: List<DayOfWeek> = (1..7).map { DayOfWeek.of(it) }

private val TitleTextStyle = TextStyle(
  fontWeight = FontWeight.Normal,
  fontSize = 12.sp,
  lineHeight = 16.sp,
  letterSpacing = TextUnit(0.004f, TextUnitType.Em),
)
private val HeadlineTextStyle = TextStyle(
  fontWeight = FontWeight.SemiBold,
  fontSize = 28.sp,
  lineHeight = 36.sp,
)
private val SubheadTextStyle = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp)
private val NavTextStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center)
private val WeekdayTextStyle = TextStyle(fontSize = 12.sp)
private val DayTextStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center)

private val defaultZoneId = ZoneId.of("UTC")
private const val DAY_MONTH_YEAR_TIME_PATTERN = "dd.MM.yyyy"
private const val MODAL_HEADLINE_TIME_PATTERN = "MMM d, yyyy"
