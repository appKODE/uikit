package ru.kode.uikit.components.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.window.DialogProperties

/** Full-width sheet dialog that doesn't dismiss on outside clicks (the sheet's own scrim handles that). */
internal expect fun sheetDialogProperties(): DialogProperties

/** Platform window tweaks for the sheet dialog; call inside the Dialog content. */
@Composable
internal expect fun SheetDialogWindowSetup()

/** M3 typography's `includeFontPadding = false` (Android-only knob; null elsewhere). */
internal expect val NoFontPaddingTextStyle: PlatformTextStyle?
