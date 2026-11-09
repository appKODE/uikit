package ru.kode.uikit.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import ru.kode.uikit.components.internal.SheetDialogWindowSetup
import ru.kode.uikit.components.internal.sheetDialogProperties
import kotlin.math.roundToInt

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
  val scope = rememberCoroutineScope()
  // Vertical translation of the sheet: 0 = fully expanded, +height = hidden below the screen.
  val offsetY = remember { Animatable(0f) }
  var sheetHeight by remember { mutableFloatStateOf(0f) }
  var revealed by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismissRequest,
    properties = sheetDialogProperties(),
  ) {
    SheetDialogWindowSetup()
    val sheetShape = RoundedCornerShape(
      topStart = configurationInternal.cornerRadius,
      topEnd = configurationInternal.cornerRadius,
    )
    val scrimFraction = if (sheetHeight > 0f) (1f - offsetY.value / sheetHeight).coerceIn(0f, 1f) else 0f
    Box(modifier.fillMaxSize()) {
      Box(
        Modifier
          .fillMaxSize()
          .background(
            configurationInternal.scrimColor.copy(
              alpha =
              configurationInternal.scrimColor.alpha * scrimFraction,
            ),
          )
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
          ) {
            scope.launch {
              offsetY.animateTo(sheetHeight, tween(ANIMATION_DURATION_MILLIS))
              onDismissRequest()
            }
          },
      )
      Column(
        Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .widthIn(max = configurationInternal.sheetMaxWidth)
          .offset { IntOffset(0, offsetY.value.roundToInt()) }
          .onSizeChanged {
            sheetHeight = it.height.toFloat()
            if (!revealed) {
              revealed = true
              scope.launch {
                offsetY.snapTo(it.height.toFloat())
                offsetY.animateTo(0f, tween(ANIMATION_DURATION_MILLIS))
              }
            }
          }
          .draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
              scope.launch { offsetY.snapTo((offsetY.value + delta).coerceAtLeast(0f)) }
            },
            onDragStopped = { velocity ->
              val shouldDismiss = offsetY.value > sheetHeight * DISMISS_POSITION_FRACTION ||
                velocity > DISMISS_VELOCITY_THRESHOLD
              if (shouldDismiss) {
                offsetY.animateTo(sheetHeight, tween(ANIMATION_DURATION_MILLIS))
                onDismissRequest()
              } else {
                offsetY.animateTo(0f, tween(ANIMATION_DURATION_MILLIS))
              }
            },
          )
          // Shape-aware background then clip, like Material3 Surface (identical corner anti-aliasing).
          .background(configurationInternal.backgroundColor, sheetShape)
          .clip(sheetShape),
      ) {
        dragHandle?.invoke()
        sheetContent()
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
      }
    }
  }
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

private const val ANIMATION_DURATION_MILLIS = 300
private const val DISMISS_POSITION_FRACTION = 0.5f
private const val DISMISS_VELOCITY_THRESHOLD = 1000f
