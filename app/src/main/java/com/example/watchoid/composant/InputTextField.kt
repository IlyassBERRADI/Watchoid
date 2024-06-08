package com.example.watchoid.composant

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun InputTextField(text: MutableState<String>, modifier: Modifier = Modifier, label: String = "Enter a value"){
    TextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        label = { Text(label) },
        modifier = modifier
    )
}