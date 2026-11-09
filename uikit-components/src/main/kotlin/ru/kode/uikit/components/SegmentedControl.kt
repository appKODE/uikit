package ru.kode.uikit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@DslMarker
annotation class SegmentedControlContainerDsl

@SegmentedControlContainerDsl
interface SegmentedControlContainerScope {
  fun toggle(
    key: Any? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean,
    configuration: (SegmentedControlToggleConfiguration) -> SegmentedControlToggleConfiguration = { it },
    content: @Composable ColumnScope.() -> Unit,
  )
}

@Composable
fun SegmentedControlContainer(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  configuration: (SegmentedControlContainerConfiguration) -> SegmentedControlContainerConfiguration = { it },
  content: SegmentedControlContainerScope.() -> Unit,
) {
  val config = configuration(LocalUikitConfiguration.current.segmentedControlContainerConfiguration)
  val colors = config.colors
  val toggles = SegmentedControlContainerScopeImpl().apply(content).toggles
  val backgroundColor by animateColorAsState(
    targetValue = if (enabled) colors.backgroundColor else colors.disabledBackgroundColor,
    animationSpec = config.colorAnimationSpec,
    label = "SegmentedControlContainerBackgroundColor",
  )
  val shape = remember(config.cornerRadius) { RoundedCornerShape(config.cornerRadius) }

  Row(
    modifier = modifier
      .clip(shape)
      .background(backgroundColor),
    horizontalArrangement = Arrangement.spacedBy(config.toggleSpacing),
  ) {
    toggles.forEach { (toggleKey, toggle) ->
      key(toggleKey) { toggle() }
    }
  }
}

@Composable
fun RowScope.SegmentedControlToggle(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  enabled: Boolean = true,
  selected: Boolean = true,
  configuration: (SegmentedControlToggleConfiguration) -> SegmentedControlToggleConfiguration = { it },
  content: @Composable ColumnScope.() -> Unit,
) {
  val config = configuration(LocalUikitConfiguration.current.segmentedControlToggleConfiguration)
  val colors = config.colors
  val targetBackgroundColor = when {
    !enabled && selected -> colors.disabledSelectedBackgroundColor
    !enabled -> colors.disabledUnselectedBackgroundColor
    selected -> colors.selectedBackgroundColor
    else -> colors.deselectedBackgroundColor
  }
  val targetContentColor = when {
    !enabled && selected -> colors.disabledSelectedContentColor
    !enabled -> colors.disabledUnselectedContentColor
    selected -> colors.selectedContentColor
    else -> colors.deselectedContentColor
  }
  val targetBorderColor = when {
    !enabled && selected -> colors.disabledSelectedBorderColor
    !enabled -> colors.disabledUnselectedBorderColor
    selected -> colors.selectedBorderColor
    else -> colors.deselectedBorderColor
  }
  val backgroundColor by animateColorAsState(
    targetValue = targetBackgroundColor,
    animationSpec = config.colorAnimationSpec,
    label = "SegmentedControlToggleBackgroundColor",
  )
  val contentColor by animateColorAsState(
    targetValue = targetContentColor,
    animationSpec = config.colorAnimationSpec,
    label = "SegmentedControlToggleContentColor",
  )
  val borderColor by animateColorAsState(
    targetValue = targetBorderColor,
    animationSpec = config.colorAnimationSpec,
    label = "SegmentedControlToggleBorderColor",
  )
  val cornerRadius by animateDpAsState(
    targetValue = if (selected) config.selectedCornerRadius else config.deselectedCornerRadius,
    animationSpec = config.cornerRadiusAnimationSpec,
    label = "SegmentedControlToggleCornerRadius",
  )
  val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }

  Row(
    modifier = modifier
      .weight(1f, fill = false)
      .clip(shape)
      .background(backgroundColor)
      .border(width = config.borderWidth, color = borderColor, shape = shape)
      .then(
        if (onClick != null) {
          Modifier.clickable(
            enabled = enabled,
            onClick = onClick,
          )
        } else {
          Modifier
        },
      )
      .padding(config.contentPadding),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      Column(modifier = Modifier.weight(1f), content = content)
    }
  }
}

private class SegmentedControlContainerScopeImpl : SegmentedControlContainerScope {
  val toggles = mutableListOf<Pair<Any, @Composable RowScope.() -> Unit>>()

  override fun toggle(
    key: Any?,
    modifier: Modifier,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    selected: Boolean,
    configuration: (SegmentedControlToggleConfiguration) -> SegmentedControlToggleConfiguration,
    content: @Composable (ColumnScope.() -> Unit),
  ) {
    toggles += (key ?: toggles.size) to @Composable {
      SegmentedControlToggle(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        selected = selected,
        configuration = configuration,
        content = content,
      )
    }
  }
}

@Immutable
data class SegmentedControlContainerConfiguration(
  val cornerRadius: Dp,
  val colors: SegmentedControlContainerColors,
  val toggleSpacing: Dp,
  val colorAnimationSpec: AnimationSpec<Color>,
)

@Immutable
interface SegmentedControlContainerColors {
  val backgroundColor: Color
  val disabledBackgroundColor: Color
}

@Immutable
data class SegmentedControlToggleConfiguration(
  val selectedCornerRadius: Dp,
  val deselectedCornerRadius: Dp,
  val contentPadding: PaddingValues,
  val borderWidth: Dp,
  val colors: SegmentedControlToggleColors,
  val colorAnimationSpec: AnimationSpec<Color>,
  val cornerRadiusAnimationSpec: AnimationSpec<Dp>,
)

@Immutable
interface SegmentedControlToggleColors {
  val selectedBackgroundColor: Color
  val selectedContentColor: Color
  val selectedBorderColor: Color
  val deselectedBackgroundColor: Color
  val deselectedContentColor: Color
  val deselectedBorderColor: Color
  val disabledSelectedBackgroundColor: Color
  val disabledSelectedContentColor: Color
  val disabledSelectedBorderColor: Color
  val disabledUnselectedBackgroundColor: Color
  val disabledUnselectedContentColor: Color
  val disabledUnselectedBorderColor: Color
}
