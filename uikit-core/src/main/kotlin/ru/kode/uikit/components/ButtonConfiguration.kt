package ru.kode.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Immutable
interface ButtonScope {
  val configuration: ButtonConfiguration
  val colors: ButtonColors
  val enabled: Boolean
  val loading: Boolean
}

@Immutable
interface ButtonConfiguration {
  val textStyle: TextStyle
  val contentPadding: PaddingValues
  val shape: Shape
  val iconSize: Dp
  val contentSpacing: Dp
  val loaderStrokeWidth: Dp
}

@Immutable
interface ButtonColors {
  val backgroundColor: Color
  val textColor: Color
  val iconColor: Color
  val loadingBackgroundColor: Color
  val loadingTextColor: Color
  val loadingIconColor: Color
  val disabledBackgroundColor: Color
  val disabledTextColor: Color
  val disabledIconColor: Color
}
