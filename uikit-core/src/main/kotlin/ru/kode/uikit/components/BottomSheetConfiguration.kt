package ru.kode.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@Immutable
data class BottomSheetConfiguration(
  val backgroundColor: Color,
  val scrimColor: Color,
  val sheetMaxWidth: Dp,
  val cornerRadius: Dp,
  val handleConfiguration: HandleConfiguration,
) {
  @Immutable
  data class HandleConfiguration(
    val color: Color,
    val width: Dp,
    val height: Dp,
    val verticalPadding: Dp,
    val shape: Shape,
  )
}

object UikitBottomSheetDefaults {
  /** Default `dragHandle` slot of [ModalBottomSheet]. */
  @Composable
  fun DragHandle(
    configuration: BottomSheetConfiguration.HandleConfiguration =
      LocalUikitConfiguration.current.bottomSheetConfiguration.handleConfiguration,
    modifier: Modifier = Modifier.fillMaxWidth(),
  ) {
    Box(
      modifier = modifier,
      contentAlignment = Alignment.Center,
    ) {
      Spacer(
        modifier = Modifier
          .padding(vertical = configuration.verticalPadding)
          .size(width = configuration.width, height = configuration.height)
          .background(
            color = configuration.color,
            shape = configuration.shape,
          ),
      )
    }
  }
}
