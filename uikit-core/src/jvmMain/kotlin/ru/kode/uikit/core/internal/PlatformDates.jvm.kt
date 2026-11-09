package ru.kode.uikit.core.internal

import java.util.Locale

// Fixed pattern; Java 19+ DateTimeFormatter.ofLocalizedPattern("yMMMM") once the toolchain moves past 17.
internal actual fun monthYearPattern(locale: Locale): String = "LLLL yyyy"
