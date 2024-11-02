package ru.kode.uikit.components.shimmer

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

fun Modifier.shimmer(
  visible: Boolean,
  configuration: (ShimmerConfiguration) -> ShimmerConfiguration = { it }
): Modifier = composed {
  val config by rememberUpdatedState(configuration(LocalUikitConfiguration.current.shimmerConfiguration))
  placeholder(
    visible = visible,
    shape = config.shape,
    color = config.color,
    highlight = PlaceholderHighlight.shimmer(highlightColor = config.highlightColor),
    placeholderFadeTransitionSpec = { tween() },
    contentFadeTransitionSpec = { tween() }
  )
}

@Immutable
data class ShimmerConfiguration(
  val shape: Shape,
  val color: Color,
  val highlightColor: Color,
)
