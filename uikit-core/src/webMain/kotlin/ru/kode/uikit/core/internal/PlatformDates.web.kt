package ru.kode.uikit.core.internal

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number

@InternalUikitApi
actual object PlatformDates {
  actual fun format(date: LocalDate, pattern: String): String = Regex("y+|M+|d+").replace(pattern) {
    val token = it.value
    when {
      token == "MMMM" -> intlMonth(date.year, date.month.number, "long")
      token == "MMM" -> intlMonth(date.year, date.month.number, "short")
      token.startsWith("M") -> date.month.number.toString().padStart(token.length, '0')
      token.startsWith("d") -> date.day.toString().padStart(token.length, '0')
      else -> date.year.toString().padStart(token.length, '0')
    }
  }

  actual fun monthYear(year: Int, month: Int): String = intlMonthYear(year, month)

  actual fun firstDayOfWeek(): DayOfWeek = DayOfWeek(intlFirstDayOfWeek())

  actual fun narrowWeekdayName(dayOfWeek: DayOfWeek): String = intlNarrowWeekday(dayOfWeek.isoDayNumber)
}

private fun intlMonth(year: Int, month: Int, style: String): String = js(
  "new Intl.DateTimeFormat(undefined, { month: style, timeZone: 'UTC' }).format(new Date(Date.UTC(year, month - 1, 1)))",
)

private fun intlMonthYear(year: Int, month: Int): String = js(
  "new Intl.DateTimeFormat(undefined, { year: 'numeric', month: 'long', timeZone: 'UTC' }).format(new Date(Date.UTC(year, month - 1, 1)))",
)

// 2024-01-01 is a Monday, so ISO day n is January n.
private fun intlNarrowWeekday(isoDay: Int): String = js(
  "new Intl.DateTimeFormat(undefined, { weekday: 'narrow', timeZone: 'UTC' }).format(new Date(Date.UTC(2024, 0, isoDay)))",
)

// Intl.Locale weekInfo (getWeekInfo() in newer engines) uses ISO numbering; default to Monday where unsupported.
private fun intlFirstDayOfWeek(): Int = js(
  "(function () { try { var l = new Intl.Locale(Intl.DateTimeFormat().resolvedOptions().locale); var w = l.getWeekInfo ? l.getWeekInfo() : l.weekInfo; return (w && w.firstDay) || 1; } catch (e) { return 1; } })()",
)
