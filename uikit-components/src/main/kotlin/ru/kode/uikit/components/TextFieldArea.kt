package ru.kode.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldArea(
  configuration: TextFieldAreaConfiguration,
  value: TextFieldValue,
  onValueChange: (TextFieldValue) -> Unit,
  modifier: Modifier = Modifier,
  header: @Composable (ColumnScope.() -> Unit)? = null,
  footer: @Composable (ColumnScope.() -> Unit)? = null,
  placeholder: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  isError: Boolean = false,
  contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
  contentHeight: Dp = 120.dp,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions(),
) {

  Column(modifier = modifier) {
    if (header != null) {
      header()
    }
    BasicTextField(
      modifier = Modifier.fillMaxWidth(),
      configuration = configuration,
      value = value,
      onValueChange = onValueChange,
      enabled = enabled,
      readOnly = readOnly,
      isError = isError,
      interactionSource = interactionSource,
      visualTransformation = visualTransformation,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      placeholder = placeholder,
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      contentPadding = contentPadding,
      contentHeight = contentHeight
    )
    if (footer != null) {
      footer()
    }
  }
}

@Composable
private fun BasicTextField(
  configuration: TextFieldAreaConfiguration,
  value: TextFieldValue,
  onValueChange: (TextFieldValue) -> Unit,
  enabled: Boolean,
  readOnly: Boolean,
  isError: Boolean,
  interactionSource: MutableInteractionSource,
  visualTransformation: VisualTransformation,
  keyboardOptions: KeyboardOptions,
  keyboardActions: KeyboardActions,
  contentPadding: PaddingValues,
  contentHeight: Dp,
  modifier: Modifier = Modifier,
  placeholder: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  val windowInfo = LocalWindowInfo.current
  val isFocused = interactionSource.collectIsFocusedAsState().value
  val isWindowFocused = windowInfo.isWindowFocused
  androidx.compose.foundation.text.BasicTextField(
    modifier = modifier.fillMaxWidth(),
    value = value,
    onValueChange = onValueChange,
    enabled = enabled,
    readOnly = readOnly,
    textStyle = configuration.textFieldStyle.copy(
      color = when {
        !enabled -> configuration.disabledTextColor
        isError -> configuration.errorTextColor
        isFocused -> configuration.focusedTextColor
        else -> configuration.unfocusedTextColor
      }
    ),
    interactionSource = interactionSource,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    cursorBrush = SolidColor(if (isError) configuration.errorCursorColor else configuration.cursorColor)
  ) { innerTextField ->
    TextFieldDecoration(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = contentHeight),
      innerTextField = innerTextField,
      fieldMinHeight = contentHeight,
      contentPadding = contentPadding,
      enabled = enabled,
      isError = isError,
      shape = configuration.shape,
      isFocused = isFocused && isWindowFocused,
      placeholder = if (value.text.isEmpty()) placeholder else null,
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      borderWidth = configuration.borderWidth,
      errorBorderWidth = configuration.errorBorderWidth,
      disabledContainerColor = configuration.disabledContainerColor,
      focusedContainerColor = configuration.focusedContainerColor,
      errorBorderColor = configuration.errorBorderColor,
      focusedBorderColor = configuration.focusedBorderColor,
      unfocusedBorderColor = configuration.unfocusedBorderColor,
      disabledBorderColor = configuration.disabledBorderColor,
      errorContainerColor = configuration.errorContainerColor,
      unfocusedContainerColor = configuration.unfocusedContainerColor
    )
  }
}

@Composable
private fun TextFieldDecoration(
  innerTextField: @Composable () -> Unit,
  focusedContainerColor: Color,
  unfocusedContainerColor: Color,
  disabledContainerColor: Color,
  errorContainerColor: Color,
  focusedBorderColor: Color,
  unfocusedBorderColor: Color,
  disabledBorderColor: Color,
  errorBorderColor: Color,
  fieldMinHeight: Dp,
  borderWidth: Dp,
  errorBorderWidth: Dp,
  shape: Shape,
  isFocused: Boolean,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
  isError: Boolean = false,
  enabled: Boolean = true,
  placeholder: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  Row(
    modifier = modifier
      .background(
        color = when {
          !enabled -> disabledContainerColor
          isError -> errorContainerColor
          isFocused -> focusedContainerColor
          else -> unfocusedContainerColor
        },
        shape = shape
      )
      .border(
        width = if (isError) errorBorderWidth else borderWidth,
        color = when {
          !enabled -> disabledBorderColor
          isError -> errorBorderColor
          isFocused -> focusedBorderColor
          else -> unfocusedBorderColor
        },
        shape = shape
      ),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (leadingIcon != null) {
      leadingIcon()
    }
    Box(
      modifier = Modifier
        .weight(1f)
        .heightIn(min = fieldMinHeight)
        .padding(contentPadding),
      contentAlignment = Alignment.CenterStart
    ) {
      if (placeholder != null) {
        placeholder()
      }
      innerTextField()
    }
    if (trailingIcon != null) {
      trailingIcon()
    }
  }
}

@Immutable
data class TextFieldAreaConfiguration(
  val textFieldStyle: TextStyle,
  val shape: Shape,
  val borderWidth: Dp,
  val errorBorderWidth: Dp,
  val focusedTextColor: Color,
  val unfocusedTextColor: Color,
  val disabledTextColor: Color,
  val errorTextColor: Color,
  val focusedContainerColor: Color,
  val unfocusedContainerColor: Color,
  val disabledContainerColor: Color,
  val errorContainerColor: Color,
  val focusedBorderColor: Color,
  val unfocusedBorderColor: Color,
  val disabledBorderColor: Color,
  val errorBorderColor: Color,
  val cursorColor: Color,
  val errorCursorColor: Color,
)
