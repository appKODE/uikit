package ru.kode.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
fun PageIndicator(
  pageCount: Int,
  currentPageIndex: Int,
  configuration: PageIndicatorConfiguration,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .background(color = configuration.backgroundColor, shape = configuration.shape)
      .padding(configuration.padding),
    horizontalArrangement = Arrangement.spacedBy(configuration.spacing),
    verticalAlignment = Alignment.CenterVertically
  ) {
    repeat(pageCount) { i ->
      val color = if (currentPageIndex == i) {
        configuration.activeColor
      } else {
        configuration.inactiveColor
      }
      Box(
        modifier = Modifier
          .size(configuration.size)
          .background(
            color = color,
            shape = CircleShape
          )
      )
    }
  }
}

@Immutable
data class PageIndicatorConfiguration(
  val size: Dp,
  val spacing: Dp,
  val padding: PaddingValues,
  val activeColor: Color,
  val inactiveColor: Color,
  val shape: Shape,
  val backgroundColor: Color,
)
