package ru.kode.uikit.core.internal

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toJavaLocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

/** Best `yMMMM` pattern for [locale]; Android asks the platform, plain JVM falls back to a fixed pattern. */
internal expect fun monthYearPattern(locale: Locale): String

@InternalUikitApi
actual object PlatformDates {
  actual fun format(date: LocalDate, pattern: String): String =
    date.toJavaLocalDate().format(DateTimeFormatter.ofPattern(pattern))

  actual fun monthYear(year: Int, month: Int): String {
    val locale = Locale.getDefault()
    return YearMonth.of(year, month).format(DateTimeFormatter.ofPattern(monthYearPattern(locale), locale))
  }

  actual fun firstDayOfWeek(): DayOfWeek = DayOfWeek(WeekFields.of(Locale.getDefault()).firstDayOfWeek.value)

  actual fun narrowWeekdayName(dayOfWeek: DayOfWeek): String =
    java.time.DayOfWeek.of(dayOfWeek.isoDayNumber).getDisplayName(TextStyle.NARROW_STANDALONE, Locale.getDefault())
}
