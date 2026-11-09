package ru.kode.uikit.core.internal

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(InternalUikitApi::class)
class PlatformDatesTest {
  private val date = LocalDate(2026, 1, 15)

  @Test
  fun numericPattern() = assertEquals("15.01.2026", PlatformDates.format(date, "dd.MM.yyyy"))

  @Test
  fun monthNamePatternKeepsDayAndYear() {
    val text = PlatformDates.format(date, "MMM d, yyyy")
    assertTrue(text.contains("15") && text.contains("2026"), text)
  }

  @Test
  fun monthYearContainsYear() = assertTrue(PlatformDates.monthYear(2026, 1).contains("2026"))

  @Test
  fun firstDayOfWeekIsValid() = assertTrue(PlatformDates.firstDayOfWeek() in DayOfWeek.entries)

  @Test
  fun narrowWeekdayNamesAreNonBlank() = DayOfWeek.entries.forEach {
    assertTrue(PlatformDates.narrowWeekdayName(it).isNotBlank(), it.name)
  }
}
