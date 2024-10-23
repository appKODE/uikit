package ru.kode.uikit.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration
import kotlin.math.abs

@Composable
fun Checkbox(
  checked: Boolean,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  error: Boolean = false,
  onClick: () -> Unit = { },
  configuration: (CheckboxConfiguration) -> CheckboxConfiguration = { it },
) {
  val configurationInternal by rememberUpdatedState(configuration(LocalUikitConfiguration.current.checkbox))
  val rippleColor = configurationInternal.color(checked = true, error = error, enabled = enabled)
  Box(
    modifier = modifier
      .size(48.dp)
      .clip(CircleShape)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(
          bounded = false,
          radius = 24.dp,
          color = rippleColor
        ),
        enabled = enabled,
        onClick = onClick
      ),
    contentAlignment = Alignment.Center
  ) {
    val transition = updateTransition(
      targetState = checked,
      label = "checked transition"
    )
    val iconChangingFraction by transition.animateFloat(
      transitionSpec = { tween(IconChangingDuration) },
      label = "icon changing transition"
    ) {
      if (it) 1f else 0f
    }
    val uncheckedIconAlpha = remember(iconChangingFraction) {
      abs(iconChangingFraction - 1f)
    }
    Icon(
      modifier = Modifier
        .alpha(uncheckedIconAlpha)
        .size(24.dp),
      painter = painterResource(configurationInternal.uncheckedIconRes),
      contentDescription = "unchecked checkbox icon",
      tint = configurationInternal.color(checked = false, error = error, enabled = enabled)
    )
    val checkedIconScale = remember(iconChangingFraction) {
      lerp(0.5f, 1f, iconChangingFraction)
    }
    Icon(
      modifier = Modifier
        .graphicsLayer(
          scaleX = checkedIconScale,
          scaleY = checkedIconScale,
          alpha = iconChangingFraction
        )
        .size(24.dp),
      painter = painterResource(configurationInternal.checkedIconRes),
      contentDescription = "checked checkbox icon",
      tint = configurationInternal.color(checked = true, error = error, enabled = enabled)
    )
  }
}

@Immutable
data class CheckboxConfiguration(
  @DrawableRes val checkedIconRes: Int,
  @DrawableRes val uncheckedIconRes: Int,
  val checkedColor: Color,
  val uncheckedColor: Color,
  val errorCheckedColor: Color,
  val errorUncheckedColor: Color,
  val disabledCheckedColor: Color,
  val disabledUncheckedColor: Color,
)

private fun CheckboxConfiguration.color(checked: Boolean, error: Boolean, enabled: Boolean): Color {
  return when {
    !enabled -> if (checked) disabledCheckedColor else disabledUncheckedColor
    error -> if (checked) errorCheckedColor else errorUncheckedColor
    else -> if (checked) checkedColor else uncheckedColor
  }
}

private const val IconChangingDuration = 100
