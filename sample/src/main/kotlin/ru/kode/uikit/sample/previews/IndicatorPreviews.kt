package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.PageIndicator
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Indicators"

@ShowkaseComposable(name = "PageIndicator", group = GROUP)
@Preview
@Composable
fun PageIndicatorPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      PageIndicator(pageCount = 4, currentPageIndex = 0)
      PageIndicator(pageCount = 4, currentPageIndex = 2)
    }
  }
}
