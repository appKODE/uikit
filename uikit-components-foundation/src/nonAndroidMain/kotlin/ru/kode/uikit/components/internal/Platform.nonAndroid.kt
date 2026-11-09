package ru.kode.uikit.components.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.window.DialogProperties

internal actual val NoFontPaddingTextStyle: PlatformTextStyle? = null

internal actual fun sheetDialogProperties() = DialogProperties(
  usePlatformDefaultWidth = false,
  dismissOnClickOutside = false,
)

@Composable
internal actual fun SheetDialogWindowSetup() = Unit
