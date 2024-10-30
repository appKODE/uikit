package ru.kode.uikit.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BottomSheetHandle(
  configuration: BottomSheetHandleConfiguration,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
  ) {
    Spacer(
      modifier = Modifier
        .padding(vertical = configuration.verticalPadding)
        .size(width = configuration.width, height = configuration.height)
        .background(
          color = configuration.color,
          shape = configuration.shape
        )
    )
  }
}
