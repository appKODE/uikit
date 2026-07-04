package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.shimmer.shimmer
import ru.kode.uikit.sample.PreviewTheme

private const val GROUP = "Shimmer"

@ShowkaseComposable(name = "Shimmer placeholders", group = GROUP)
@Preview
@Composable
fun ShimmerPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(20.dp)
          .shimmer(visible = true),
      )
      Box(
        modifier = Modifier
          .width(160.dp)
          .height(20.dp)
          .shimmer(visible = true),
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(80.dp)
          .shimmer(visible = true),
      )
    }
  }
}
