package ru.kode.uikit.components.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
  configuration: BottomSheetConfiguration,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  handle: (@Composable () -> Unit)? = if (configuration.handleConfiguration != null) {
    { BottomSheetHandle(configuration.handleConfiguration) }
  } else null,
  sheetContent: @Composable ColumnScope.() -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  androidx.compose.material3.ModalBottomSheet(
    modifier = modifier,
    containerColor = configuration.backgroundColor,
    scrimColor = configuration.scrimColor,
    sheetMaxWidth = configuration.sheetMaxWidth,
    shape = RoundedCornerShape(topStart = configuration.cornerRadius, topEnd = configuration.cornerRadius),
    dragHandle = handle,
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    contentWindowInsets = { WindowInsets.navigationBars },
    content = sheetContent
  )
}

@Immutable
data class BottomSheetConfiguration(
  val backgroundColor: Color,
  val scrimColor: Color,
  val sheetMaxWidth: Dp,
  val cornerRadius: Dp,
  val handleConfiguration: BottomSheetHandleConfiguration?,
)

@Immutable
data class BottomSheetHandleConfiguration(
  val color: Color,
  val width: Dp,
  val height: Dp,
  val verticalPadding: Dp,
  val shape: Shape,
)
