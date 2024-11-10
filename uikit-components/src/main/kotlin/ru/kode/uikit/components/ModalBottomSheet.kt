package ru.kode.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  configuration: (BottomSheetConfiguration) -> BottomSheetConfiguration = { it },
  showHandle: Boolean = true,
  sheetContent: @Composable ColumnScope.() -> Unit,
) {
  val configurationInternal by rememberUpdatedState(
    configuration(LocalUikitConfiguration.current.bottomSheetConfiguration)
  )
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  androidx.compose.material3.ModalBottomSheet(
    modifier = modifier,
    containerColor = configurationInternal.backgroundColor,
    scrimColor = configurationInternal.scrimColor,
    sheetMaxWidth = configurationInternal.sheetMaxWidth,
    shape = RoundedCornerShape(
      topStart = configurationInternal.cornerRadius,
      topEnd = configurationInternal.cornerRadius
    ),
    dragHandle = if (showHandle) {
      {
        BottomSheetHandle(
          configuration = configurationInternal.handleConfiguration,
          modifier = Modifier.fillMaxWidth()
        )
      }
    } else {
      null
    },
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    contentWindowInsets = { WindowInsets.navigationBars },
    content = sheetContent
  )
}

@Composable
private fun BottomSheetHandle(
  configuration: BottomSheetConfiguration.HandleConfiguration,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier,
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
