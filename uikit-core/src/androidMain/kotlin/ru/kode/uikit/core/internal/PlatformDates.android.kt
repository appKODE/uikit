package ru.kode.uikit.core.internal

import android.text.format.DateFormat
import java.util.Locale

internal actual fun monthYearPattern(locale: Locale): String = DateFormat.getBestDateTimePattern(locale, "yMMMM")
