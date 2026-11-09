package ru.kode.uikit.core.internal

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeZoneWithName

@InternalUikitApi
actual object PlatformDates {
  actual fun format(date: LocalDate, pattern: String): String =
    formatter().apply { dateFormat = pattern }.stringFromDate(date.toNSDate())

  actual fun monthYear(year: Int, month: Int): String = formatter().apply {
    setLocalizedDateFormatFromTemplate("yMMMM")
  }.stringFromDate(LocalDate(year, month, 1).toNSDate())

  // NSCalendar.firstWeekday: 1 = Sunday ... 7 = Saturday.
  actual fun firstDayOfWeek(): DayOfWeek = DayOfWeek((NSCalendar.currentCalendar.firstWeekday.toInt() + 5) % 7 + 1)

  // Symbols are indexed from Sunday.
  actual fun narrowWeekdayName(dayOfWeek: DayOfWeek): String =
    formatter().veryShortStandaloneWeekdaySymbols[dayOfWeek.isoDayNumber % 7] as String

  private fun formatter() = NSDateFormatter().apply {
    locale = NSLocale.currentLocale
    timeZone = NSTimeZone.timeZoneWithName("UTC")!!
  }

  private fun LocalDate.toNSDate(): NSDate =
    NSDate.dateWithTimeIntervalSince1970(atStartOfDayIn(TimeZone.UTC).epochSeconds.toDouble())
}
