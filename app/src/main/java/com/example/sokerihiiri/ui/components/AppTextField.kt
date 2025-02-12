package com.example.sokerihiiri.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/*
Tyylitellyt kentät, jotta näyttävät varmasti samalta koko sovelluksessa.
 */

@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable() (() -> Unit)?,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = {
            defaultKeyboardAction(ImeAction.Done)
        }
    ),
    enabled: Boolean = true,
    isError: Boolean = false
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        colors = colors,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        isError = isError
    )
}

@Composable
fun NumberTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable() (() -> Unit)?,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Number
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = {
            defaultKeyboardAction(ImeAction.Done)
        }
    ),
    enabled: Boolean = true,
    isError: Boolean = false
) {
    StyledTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        isError = isError
    )
}