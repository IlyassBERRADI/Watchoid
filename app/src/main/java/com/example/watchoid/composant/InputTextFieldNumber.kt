package com.example.watchoid.composant

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputTextFieldNumber(text: MutableState<String>, modifier: Modifier = Modifier, label: String = "Enter a value"){
    TextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}
