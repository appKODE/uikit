package ru.kode.uikit.components.shimmer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun Shimmer(
  width: Dp,
  height: Dp,
  modifier: Modifier = Modifier,
  configuration: (ShimmerConfiguration) -> ShimmerConfiguration = { it },
) {
  Spacer(
    modifier = modifier
      .shimmer(
        visible = true,
        configuration = configuration
      )
      .size(width, height)
  )
}
