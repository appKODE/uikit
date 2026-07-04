package ru.kode.uikit.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

/**
 * Wraps preview content with a concrete [LocalUikitConfiguration] so uikit components render.
 * Every gallery preview must be wrapped in this, otherwise `LocalUikitConfiguration` throws.
 */
@Composable
fun PreviewTheme(content: @Composable () -> Unit) {
  val configuration = remember { previewUikitConfiguration() }
  CompositionLocalProvider(LocalUikitConfiguration provides configuration) {
    MaterialTheme {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(PreviewTokens.surface)
          .padding(16.dp),
      ) {
        content()
      }
    }
  }
}
