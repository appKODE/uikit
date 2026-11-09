package ru.kode.uikit.sample.previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import ru.kode.uikit.components.Cell
import ru.kode.uikit.components.CellContainer
import ru.kode.uikit.sample.PreviewTheme
import ru.kode.uikit.sample.PreviewTokens

private const val GROUP = "Cell"

@Composable
private fun Chevron() {
  Icon(
    painter = painterResource(android.R.drawable.arrow_down_float),
    contentDescription = null,
    modifier = Modifier.size(16.dp),
  )
}

@Composable
private fun Avatar() {
  Icon(
    painter = painterResource(android.R.drawable.ic_menu_myplaces),
    contentDescription = null,
    modifier = Modifier.size(24.dp),
  )
}

@ShowkaseComposable(name = "Cell — states", group = GROUP)
@Preview
@Composable
fun CellStatesPreview() {
  PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Cell { Text("Title only") }
      Cell(onClick = {}, chevronContent = { Chevron() }) {
        Text("Clickable with chevron")
      }
      Cell(
        leadingContent = { Avatar() },
        trailingContent = { Text("Value") },
        chevronContent = { Chevron() },
      ) {
        Text("Leading + trailing")
        Text("Subtitle", style = PreviewTokens.helperStyle)
      }
      Cell(
        onClick = {},
        enabled = false,
        leadingContent = { Avatar() },
        trailingContent = { Text("Value") },
        chevronContent = { Chevron() },
      ) {
        Text("Disabled")
      }
    }
  }
}

@ShowkaseComposable(name = "CellContainer — grouped cells", group = GROUP)
@Preview
@Composable
fun CellContainerPreview() {
  PreviewTheme {
    CellContainer {
      cell(onClick = {}, leadingContent = { Avatar() }, chevronContent = { Chevron() }) {
        Text("Profile")
      }
      cell(onClick = {}, trailingContent = { Text("On") }, chevronContent = { Chevron() }) {
        Text("Notifications")
        Text("Push and email", style = PreviewTokens.helperStyle)
      }
      cell(onClick = {}, enabled = false, chevronContent = { Chevron() }) {
        Text("Payment methods")
      }
    }
  }
}