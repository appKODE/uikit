package ru.kode.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import ru.kode.uikit.components.configuration.LocalUikitConfiguration

@DslMarker
annotation class CellContainerDsl

@CellContainerDsl
interface CellContainerScope {
  fun cell(
    key: Any? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    chevronContent: @Composable (() -> Unit)? = null,
    configuration: (CellConfiguration) -> CellConfiguration = { it },
    content: @Composable ColumnScope.() -> Unit,
  )
}

@Composable
fun CellContainer(
  modifier: Modifier = Modifier,
  configuration: (CellContainerConfiguration) -> CellContainerConfiguration = { it },
  content: CellContainerScope.() -> Unit,
) {
  val config = configuration(LocalUikitConfiguration.current.cellContainerConfiguration)
  val cells = CellContainerScopeImpl().apply(content).cells
  Column(
    modifier = modifier
      .clip(config.shape)
      .background(config.colors.backgroundColor)
  ) {
    cells.forEachIndexed { index, (cellKey, cell) ->
      key(cellKey) { cell() }
      if (index != cells.lastIndex) {
        HorizontalDivider(
          modifier = Modifier.padding(config.dividerPadding),
          thickness = config.dividerThickness,
          color = config.colors.dividerColor
        )
      }
    }
  }
}

@Composable
fun Cell(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  enabled: Boolean = true,
  leadingContent: @Composable (() -> Unit)? = null,
  trailingContent: @Composable (() -> Unit)? = null,
  chevronContent: @Composable (() -> Unit)? = null,
  configuration: (CellConfiguration) -> CellConfiguration = { it },
  content: @Composable ColumnScope.() -> Unit,
) {
  val config = configuration(LocalUikitConfiguration.current.cellConfiguration)
  val colors = config.colors
  val backgroundColor = remember(colors, enabled) {
    if (enabled) colors.backgroundColor else colors.disabledBackgroundColor
  }
  val contentColor = remember(colors, enabled) {
    if (enabled) colors.contentColor else colors.disabledContentColor
  }
  val chevronColor = remember(colors, enabled) {
    if (enabled) colors.chevronColor else colors.disabledChevronColor
  }
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(backgroundColor)
      .then(if (onClick != null) Modifier.clickable(enabled = enabled, onClick = onClick) else Modifier)
      .padding(config.contentPadding),
    horizontalArrangement = Arrangement.spacedBy(config.contentSpacing),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      leadingContent?.invoke()
      Column(modifier = Modifier.weight(1f), content = content)
      trailingContent?.invoke()
    }
    CompositionLocalProvider(LocalContentColor provides chevronColor) {
      chevronContent?.invoke()
    }
  }
}

private class CellContainerScopeImpl : CellContainerScope {
  val cells = mutableListOf<Pair<Any, @Composable () -> Unit>>()

  override fun cell(
    key: Any?,
    modifier: Modifier,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    leadingContent: @Composable (() -> Unit)?,
    trailingContent: @Composable (() -> Unit)?,
    chevronContent: @Composable (() -> Unit)?,
    configuration: (CellConfiguration) -> CellConfiguration,
    content: @Composable ColumnScope.() -> Unit,
  ) {
    cells += (key ?: cells.size) to @Composable {
      Cell(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        chevronContent = chevronContent,
        configuration = configuration,
        content = content
      )
    }
  }
}

@Immutable
data class CellContainerConfiguration(
  val shape: Shape,
  val colors: CellContainerColors,
  val dividerThickness: Dp,
  val dividerPadding: PaddingValues,
)

@Immutable
interface CellContainerColors {
  val backgroundColor: Color
  val dividerColor: Color
}

@Immutable
data class CellConfiguration(
  val contentPadding: PaddingValues,
  val contentSpacing: Dp,
  val colors: CellColors,
)

@Immutable
interface CellColors {
  val backgroundColor: Color
  val contentColor: Color
  val chevronColor: Color
  val disabledBackgroundColor: Color
  val disabledContentColor: Color
  val disabledChevronColor: Color
}