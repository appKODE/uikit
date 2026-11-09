package ru.kode.uikit.components.internal

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Suppress("DEPRECATION")
internal actual val NoFontPaddingTextStyle: PlatformTextStyle? = PlatformTextStyle(includeFontPadding = false)

internal actual fun sheetDialogProperties() = DialogProperties(
  usePlatformDefaultWidth = false,
  dismissOnClickOutside = false,
  decorFitsSystemWindows = false,
)

// Match the Material3 sheet window: full-screen, no platform dim (the sheet's scrim is the only dim).
@Composable
internal actual fun SheetDialogWindowSetup() {
  val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
  SideEffect {
    dialogWindow?.setDimAmount(0f)
    dialogWindow?.setLayout(MATCH_PARENT, MATCH_PARENT)
  }
}
