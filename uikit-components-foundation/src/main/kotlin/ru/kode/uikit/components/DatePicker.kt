package ru.kode.uikit.components

import android.text.format.DateFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
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
import java.time.temporal.WeekFields
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
  title: @Composable (ModalDatePickerConfiguration) -> Unit = { UikitDatePickerDefaults.Title(it) },
  headline: @Composable (LocalDate) -> Unit = { UikitDatePickerDefaults.Headline(it) },
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
      title = title,
      headline = headline,
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

// Layout ported from Material3 1.4 BasicAlertDialog + DatePicker (DatePickerModalTokens), Apache-2.0.
@Composable
private fun ModalDatePicker(
  date: LocalDate,
  configuration: ModalDatePickerConfiguration,
  onDismissRequest: () -> Unit,
  onDateConfirm: (LocalDate) -> Unit,
  yearRange: IntRange = UikitDatePickerDefaults.YearRange,
  selectableDates: SelectableDates = UikitDatePickerDefaults.AllDates,
  title: @Composable (ModalDatePickerConfiguration) -> Unit = { UikitDatePickerDefaults.Title(it) },
  headline: @Composable (LocalDate) -> Unit = { UikitDatePickerDefaults.Headline(it) },
) {
  val locale = Locale.getDefault()
  var displayedMonth by remember { mutableStateOf(YearMonth.from(date)) }
  var showYearPicker by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Box(Modifier.sizeIn(minWidth = 280.dp, maxWidth = 560.dp), propagateMinConstraints = true) {
      Column(
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .clip(RoundedCornerShape(16.dp))
          .sizeIn(minWidth = 360.dp)
          .fillMaxWidth()
          .background(configuration.containerColor),
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp),
          verticalArrangement = Arrangement.SpaceBetween,
        ) {
          Box(contentAlignment = Alignment.BottomStart) { title(configuration) }
          Column(Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth()) { headline(date) }
            Divider(configuration.dividerColor)
          }
        }
        MonthsNavigation(
          text = displayedMonth.format(
            DateTimeFormatter.ofPattern(DateFormat.getBestDateTimePattern(locale, "yMMMM"), locale),
          ),
          yearPickerVisible = showYearPicker,
          previousAvailable = displayedMonth > YearMonth.of(yearRange.first, 1),
          nextAvailable = displayedMonth < YearMonth.of(yearRange.last, 12),
          color = configuration.navigationContentColor,
          onYearPickerClick = { showYearPicker = !showYearPicker },
          onPreviousClick = { displayedMonth = displayedMonth.minusMonths(1) },
          onNextClick = { displayedMonth = displayedMonth.plusMonths(1) },
        )
        Box {
          val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
          Column(Modifier.padding(horizontal = 12.dp)) {
            WeekDays(firstDayOfWeek, locale, configuration.weekdayContentColor)
            Month(displayedMonth, firstDayOfWeek, date, selectableDates, configuration, onDateConfirm)
          }
          if (showYearPicker) {
            Column(Modifier.background(configuration.containerColor)) {
              YearPicker(
                modifier = Modifier.requiredHeight(MinTouchSize * (MAX_CALENDAR_ROWS + 1) - 1.dp),
                yearRange = yearRange,
                selectedYear = displayedMonth.year,
                selectableDates = selectableDates,
                configuration = configuration,
                onYearSelected = {
                  displayedMonth = displayedMonth.withYear(it)
                  showYearPicker = false
                },
              )
              Divider(configuration.dividerColor)
            }
          }
        }
      }
    }
  }
}

/** Default `title` slot of [TextFieldDatePicker]'s modal: the configured title. */
@Composable
fun UikitDatePickerDefaults.Title(configuration: ModalDatePickerConfiguration) {
  UikitText(
    modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
    text = stringResource(configuration.titleRes),
    style = TitleTextStyle,
    color = Color(0xFF000000).copy(alpha = 0.7f),
  )
}

/** Default `headline` slot of [TextFieldDatePicker]'s modal: the shown date. */
@Composable
fun UikitDatePickerDefaults.Headline(date: LocalDate) {
  UikitText(
    modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
    text = date.format(DateTimeFormatter.ofPattern(MODAL_HEADLINE_TIME_PATTERN)),
    style = HeadlineTextStyle,
    color = Color(0xFF000000),
  )
}

@Composable
private fun Divider(color: Color) {
  Canvas(Modifier.fillMaxWidth().height(1.dp)) {
    val y = size.height / 2
    drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = size.height)
  }
}

/** Equivalent of M3 `minimumInteractiveComponentSize`: at least 48.dp, content centered. */
@Composable
private fun MinTouchTarget(content: @Composable () -> Unit) {
  Box(Modifier.sizeIn(minWidth = MinTouchSize, minHeight = MinTouchSize), contentAlignment = Alignment.Center) {
    content()
  }
}

@Composable
private fun MonthsNavigation(
  text: String,
  yearPickerVisible: Boolean,
  previousAvailable: Boolean,
  nextAvailable: Boolean,
  color: Color,
  onYearPickerClick: () -> Unit,
  onPreviousClick: () -> Unit,
  onNextClick: () -> Unit,
) {
  Row(
    modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth().requiredHeight(56.dp),
    horizontalArrangement = if (yearPickerVisible) Arrangement.Start else Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    MinTouchTarget {
      Row(
        modifier = Modifier
          .clip(CircleShape)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = uikitRipple(color = Color.Black),
            onClick = onYearPickerClick,
          )
          .defaultMinSize(minWidth = 58.dp, minHeight = 40.dp)
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        UikitText(text = text, style = LabelLargeTextStyle, color = color)
        Spacer(Modifier.size(8.dp))
        // M3 tints this with LocalContentColor, which defaults to black outside a Surface.
        UikitIcon(
          painter = rememberVectorPainter(ArrowDropDownIcon),
          contentDescription = null,
          modifier = Modifier.rotate(if (yearPickerVisible) 180f else 0f),
          tint = Color.Black,
        )
      }
    }
    if (!yearPickerVisible) {
      Row {
        NavButton(ArrowLeftIcon, color, previousAvailable, onPreviousClick)
        NavButton(ArrowRightIcon, color, nextAvailable, onNextClick)
      }
    }
  }
}

@Composable
private fun NavButton(icon: ImageVector, color: Color, enabled: Boolean, onClick: () -> Unit) {
  MinTouchTarget {
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .clickable(
          enabled = enabled,
          interactionSource = remember { MutableInteractionSource() },
          indication = uikitRipple(color = color),
          onClick = onClick,
        ),
      contentAlignment = Alignment.Center,
    ) {
      UikitIcon(
        painter = rememberVectorPainter(icon),
        contentDescription = null,
        tint = if (enabled) color else color.copy(alpha = 0.38f),
      )
    }
  }
}

@Composable
private fun WeekDays(firstDayOfWeek: DayOfWeek, locale: Locale, color: Color) {
  Row(
    modifier = Modifier.defaultMinSize(minHeight = MinTouchSize).fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    repeat(DAYS_IN_WEEK) {
      Box(Modifier.size(MinTouchSize), contentAlignment = Alignment.Center) {
        UikitText(
          text = firstDayOfWeek.plus(it.toLong()).getDisplayName(JavaTextStyle.NARROW_STANDALONE, locale),
          modifier = Modifier.wrapContentSize(),
          style = BodyLargeCenteredTextStyle,
          color = color,
        )
      }
    }
  }
}

@Composable
private fun Month(
  month: YearMonth,
  firstDayOfWeek: DayOfWeek,
  selectedDate: LocalDate,
  selectableDates: SelectableDates,
  configuration: ModalDatePickerConfiguration,
  onDaySelected: (LocalDate) -> Unit,
) {
  val leadingBlanks = (month.atDay(1).dayOfWeek.value - firstDayOfWeek.value + DAYS_IN_WEEK) % DAYS_IN_WEEK
  val today = LocalDate.now()
  Column(
    modifier = Modifier.requiredHeight(MinTouchSize * MAX_CALENDAR_ROWS),
    verticalArrangement = Arrangement.SpaceEvenly,
  ) {
    for (week in 0 until MAX_CALENDAR_ROWS) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        for (day in 0 until DAYS_IN_WEEK) {
          val dayOfMonth = week * DAYS_IN_WEEK + day - leadingBlanks + 1
          if (dayOfMonth !in 1..month.lengthOfMonth()) {
            Spacer(Modifier.size(MinTouchSize))
          } else {
            val cellDate = month.atDay(dayOfMonth)
            DayCell(
              date = cellDate,
              selected = cellDate == selectedDate,
              isToday = cellDate == today,
              enabled = selectableDates.isSelectableYear(cellDate.year) &&
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

@Composable
private fun DayCell(
  date: LocalDate,
  selected: Boolean,
  isToday: Boolean,
  enabled: Boolean,
  configuration: ModalDatePickerConfiguration,
  onClick: () -> Unit,
) {
  val contentColor = when {
    selected && enabled -> configuration.selectedDayContentColor
    selected -> configuration.disabledSelectedDayContentColor
    !enabled -> configuration.disabledDayContentColor
    isToday -> configuration.todayContentColor
    else -> configuration.dayContentColor
  }
  val containerColor = when {
    !selected -> Color.Transparent
    enabled -> configuration.selectedDayContainerColor
    else -> configuration.disabledSelectedDayContainerColor
  }
  MinTouchTarget {
    Box(
      modifier = Modifier
        .then(
          if (isToday &&
            !selected
          ) {
            Modifier.border(1.dp, configuration.todayDateBorderColor, CircleShape)
          } else {
            Modifier
          },
        )
        .background(containerColor, CircleShape)
        .clip(CircleShape)
        .clickable(
          enabled = enabled,
          interactionSource = remember { MutableInteractionSource() },
          indication = uikitRipple(color = contentColor),
          onClick = onClick,
        )
        .requiredSize(40.dp),
      contentAlignment = Alignment.Center,
    ) {
      UikitText(text = date.dayOfMonth.toString(), style = BodyLargeCenteredTextStyle, color = contentColor)
    }
  }
}

@Composable
private fun YearPicker(
  modifier: Modifier,
  yearRange: IntRange,
  selectedYear: Int,
  selectableDates: SelectableDates,
  configuration: ModalDatePickerConfiguration,
  onYearSelected: (Int) -> Unit,
) {
  val years = remember(yearRange) { yearRange.toList() }
  LazyVerticalGrid(
    columns = GridCells.Fixed(3),
    modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp),
    state = rememberLazyGridState((selectedYear - yearRange.first).coerceAtLeast(0)),
    verticalArrangement = Arrangement.spacedBy(16.dp),
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
      Box(contentAlignment = Alignment.Center) {
        Box(
          modifier = Modifier
            .size(width = 72.dp, height = 36.dp)
            .clip(CircleShape)
            .background(if (selected) configuration.selectedYearContainerColor else Color.Transparent)
            .clickable(
              enabled = selectable,
              interactionSource = remember { MutableInteractionSource() },
              indication = uikitRipple(color = contentColor),
              onClick = { onYearSelected(year) },
            ),
          contentAlignment = Alignment.Center,
        ) {
          UikitText(text = year.toString(), style = BodyLargeCenteredTextStyle, color = contentColor)
        }
      }
    }
  }
}

private fun LocalDate.toUtcMillis(): Long = atStartOfDay(defaultZoneId).toInstant().toEpochMilli()

private fun materialIcon(name: String, pathBuilder: PathBuilder.() -> Unit): ImageVector =
  ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f, autoMirror = true)
    .path(fill = SolidColor(Color.Black), pathBuilder = pathBuilder)
    .build()

private val ArrowDropDownIcon = materialIcon("ArrowDropDown") {
  moveTo(7f, 10f)
  lineToRelative(5f, 5f)
  lineToRelative(5f, -5f)
  close()
}
private val ArrowLeftIcon = materialIcon("KeyboardArrowLeft") {
  moveTo(15.41f, 16.59f)
  lineTo(10.83f, 12.0f)
  lineToRelative(4.58f, -4.59f)
  lineTo(14.0f, 6.0f)
  lineToRelative(-6.0f, 6.0f)
  lineToRelative(6.0f, 6.0f)
  lineToRelative(1.41f, -1.41f)
  close()
}
private val ArrowRightIcon = materialIcon("KeyboardArrowRight") {
  moveTo(8.59f, 16.59f)
  lineTo(13.17f, 12.0f)
  lineTo(8.59f, 7.41f)
  lineTo(10.0f, 6.0f)
  lineToRelative(6.0f, 6.0f)
  lineToRelative(-6.0f, 6.0f)
  lineToRelative(-1.41f, -1.41f)
  close()
}

private val MinTouchSize = 48.dp
private const val MAX_CALENDAR_ROWS = 6
private const val DAYS_IN_WEEK = 7

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

// M3 typography defaults (tokens/TypographyTokens.kt).
@Suppress("DEPRECATION")
private val M3DefaultTextStyle = TextStyle(
  platformStyle = PlatformTextStyle(includeFontPadding = false),
  lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None),
  fontFamily = FontFamily.SansSerif,
)
private val BodyLargeCenteredTextStyle = M3DefaultTextStyle.copy(
  fontWeight = FontWeight.Normal,
  fontSize = 16.sp,
  lineHeight = 24.sp,
  letterSpacing = 0.5.sp,
  textAlign = TextAlign.Center,
)
private val LabelLargeTextStyle = M3DefaultTextStyle.copy(
  fontWeight = FontWeight.Medium,
  fontSize = 14.sp,
  lineHeight = 20.sp,
  letterSpacing = 0.1.sp,
)

private val defaultZoneId = ZoneId.of("UTC")
private const val DAY_MONTH_YEAR_TIME_PATTERN = "dd.MM.yyyy"
private const val MODAL_HEADLINE_TIME_PATTERN = "MMM d, yyyy"
