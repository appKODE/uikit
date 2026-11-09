package ru.kode.uikit.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import ru.kode.uikit.components.internal.UikitCircularLoader
import ru.kode.uikit.components.internal.UikitIcon
import ru.kode.uikit.components.internal.UikitText
import ru.kode.uikit.components.internal.uikitRipple

@Composable
fun Button(
  configuration: ButtonConfiguration,
  colors: ButtonColors,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  loading: Boolean = false,
  loadingIndicator: @Composable () -> Unit = {
    UikitButtonDefaults.LoadingIndicator(configuration, colors, enabled)
  },
  content: @Composable ButtonScope.() -> Unit,
) {
  val backgroundColor = remember(colors, enabled, loading) {
    when {
      !enabled -> colors.disabledBackgroundColor
      loading -> colors.loadingBackgroundColor
      else -> colors.backgroundColor
    }
  }
  val rippleColor = remember(colors, loading) {
    if (loading) colors.loadingTextColor else colors.textColor
  }
  Box(
    modifier = modifier
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = uikitRipple(color = rippleColor),
        enabled = enabled,
        onClick = onClick,
      )
      .background(backgroundColor, configuration.shape)
      .clip(configuration.shape)
      .padding(configuration.contentPadding),
    contentAlignment = Alignment.Center,
  ) {
    val scope = remember(configuration, colors, enabled, loading) {
      object : ButtonScope {
        override val configuration: ButtonConfiguration = configuration
        override val colors: ButtonColors = colors
        override val enabled: Boolean = enabled
        override val loading: Boolean = loading
      }
    }
    CompositionLocalProvider(LocalLoadingIndicator provides loadingIndicator) {
      content(scope)
    }
  }
}

object Button {
  @Composable
  fun ButtonScope.Text(text: String) {
    val color = remember(colors, enabled) {
      if (enabled) colors.textColor else colors.disabledTextColor
    }
    val loadingTransition = loadingTransition(loading)
    UikitText(
      modifier = Modifier.alpha(loadingTransition.contentAlpha.value),
      text = text,
      style = configuration.textStyle,
      color = color,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
    CircularLoader(Modifier.alpha(loadingTransition.loaderAlpha.value))
  }

  @Composable
  fun ButtonScope.Icon(painter: Painter, contentDescription: String?) {
    val tint = remember(colors, enabled) {
      if (enabled) colors.iconColor else colors.disabledIconColor
    }
    val loadingTransition = loadingTransition(loading)
    UikitIcon(
      modifier = Modifier
        .alpha(loadingTransition.contentAlpha.value)
        .size(configuration.iconSize),
      painter = painter,
      contentDescription = contentDescription,
      tint = tint,
    )
    CircularLoader(Modifier.alpha(loadingTransition.loaderAlpha.value))
  }

  @Composable
  fun ButtonScope.TextWithIcon(
    text: String,
    painter: Painter,
    contentDescription: String?,
    iconPosition: IconPosition = IconPosition.Leading,
  ) {
    val loadingTransition = loadingTransition(loading)
    Row(
      modifier = Modifier.alpha(loadingTransition.contentAlpha.value),
      horizontalArrangement = Arrangement.spacedBy(configuration.contentSpacing),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val iconTint = remember(colors, enabled) {
        if (enabled) colors.iconColor else colors.disabledIconColor
      }
      if (iconPosition == IconPosition.Leading) {
        UikitIcon(
          modifier = Modifier.size(configuration.iconSize),
          painter = painter,
          contentDescription = contentDescription,
          tint = iconTint,
        )
      }
      val textColor = remember(colors, enabled, loading) {
        when {
          !enabled -> colors.disabledTextColor
          loading -> colors.loadingTextColor
          else -> colors.textColor
        }
      }
      UikitText(
        text = text,
        style = configuration.textStyle,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      if (iconPosition == IconPosition.Trailing) {
        UikitIcon(
          modifier = Modifier.size(configuration.iconSize),
          painter = painter,
          contentDescription = contentDescription,
          tint = iconTint,
        )
      }
    }
    CircularLoader(Modifier.alpha(loadingTransition.loaderAlpha.value))
  }

  /** Shows the Button's `loadingIndicator` slot. */
  @Composable
  fun ButtonScope.CircularLoader(modifier: Modifier = Modifier) {
    Box(modifier) {
      LocalLoadingIndicator.current?.invoke() ?: UikitButtonDefaults.LoadingIndicator(configuration, colors, enabled)
    }
  }

  enum class IconPosition {
    Leading,
    Trailing,
  }
}

object UikitButtonDefaults {
  /** Default `loadingIndicator` slot: a spinner sized to [ButtonConfiguration.iconSize]. */
  @Composable
  fun LoadingIndicator(configuration: ButtonConfiguration, colors: ButtonColors, enabled: Boolean) {
    val color = remember(colors, enabled) {
      if (enabled) colors.loadingIconColor else colors.disabledIconColor
    }
    UikitCircularLoader(
      modifier = Modifier.size(configuration.iconSize),
      color = color,
      strokeWidth = configuration.loaderStrokeWidth,
    )
  }
}

private val LocalLoadingIndicator = compositionLocalOf<(@Composable () -> Unit)?> { null }

@Composable
private fun loadingTransition(loading: Boolean): LoadingTransition {
  val loadingTransition = updateTransition(
    targetState = loading,
    label = "loading transition",
  )
  val loaderAlpha = loadingTransition.animateFloat(
    transitionSpec = {
      if (targetState) {
        tween(durationMillis = 90, delayMillis = 90)
      } else {
        tween(durationMillis = 90)
      }
    },
    label = "loader alpha fraction",
  ) {
    if (it) 1f else 0f
  }
  val contentAlpha = loadingTransition.animateFloat(
    transitionSpec = {
      if (targetState) {
        tween(durationMillis = 90)
      } else {
        tween(durationMillis = 90, delayMillis = 90)
      }
    },
    label = "content alpha fraction",
  ) {
    if (it) 0f else 1f
  }

  return remember {
    LoadingTransition(
      loaderAlpha = loaderAlpha,
      contentAlpha = contentAlpha,
    )
  }
}

@Stable
private data class LoadingTransition(val loaderAlpha: State<Float>, val contentAlpha: State<Float>)
