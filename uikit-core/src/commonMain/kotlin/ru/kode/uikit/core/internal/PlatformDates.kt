package ru.kode.uikit.core.internal

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/** Locale-aware date text for the date pickers, using the platform's current locale. */
@InternalUikitApi
expect object PlatformDates {
  /** Formats [date] with an ICU/java.time-style [pattern]; supports `d`, `dd`, `MM`, `MMM`, `MMMM`, `yyyy`. */
  fun format(date: LocalDate, pattern: String): String

  /** Localized month + year title, the `yMMMM` skeleton (e.g. "January 2026"). [month] is 1-based. */
  fun monthYear(year: Int, month: Int): String

  fun firstDayOfWeek(): DayOfWeek

  /** Narrow standalone weekday name (e.g. "M"). */
  fun narrowWeekdayName(dayOfWeek: DayOfWeek): String
}
