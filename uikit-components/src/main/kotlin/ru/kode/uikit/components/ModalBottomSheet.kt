package ru.kode.uikit.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  configuration: (BottomSheetConfiguration) -> BottomSheetConfiguration = { it },
  dragHandle: (@Composable () -> Unit)? = {
    UikitBottomSheetDefaults.DragHandle(
      configuration(LocalUikitConfiguration.current.bottomSheetConfiguration).handleConfiguration,
    )
  },
  sheetContent: @Composable ColumnScope.() -> Unit,
) {
  val configurationInternal by rememberUpdatedState(
    configuration(LocalUikitConfiguration.current.bottomSheetConfiguration),
  )
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  androidx.compose.material3.ModalBottomSheet(
    modifier = modifier,
    containerColor = configurationInternal.backgroundColor,
    scrimColor = configurationInternal.scrimColor,
    sheetMaxWidth = configurationInternal.sheetMaxWidth,
    shape = RoundedCornerShape(
      topStart = configurationInternal.cornerRadius,
      topEnd = configurationInternal.cornerRadius,
    ),
    dragHandle = dragHandle,
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
    contentWindowInsets = { WindowInsets.navigationBars },
    content = sheetContent,
  )
}

@Deprecated("Use the dragHandle slot; pass null to hide the handle.")
@Composable
fun ModalBottomSheet(
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  configuration: (BottomSheetConfiguration) -> BottomSheetConfiguration = { it },
  showHandle: Boolean,
  sheetContent: @Composable ColumnScope.() -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    modifier = modifier,
    configuration = configuration,
    dragHandle = if (showHandle) {
      {
        UikitBottomSheetDefaults.DragHandle(
          configuration(LocalUikitConfiguration.current.bottomSheetConfiguration).handleConfiguration,
        )
      }
    } else {
      null
    },
    sheetContent = sheetContent,
  )
}
