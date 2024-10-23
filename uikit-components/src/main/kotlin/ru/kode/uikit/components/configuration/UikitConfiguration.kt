package ru.kode.uikit.components.configuration

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import ru.kode.uikit.components.CheckboxConfiguration

@Immutable
data class UikitConfiguration(
  val checkbox: CheckboxConfiguration,
)

val LocalUikitConfiguration = staticCompositionLocalOf<UikitConfiguration> {
  error("no UikitConfiguration provided")
}
