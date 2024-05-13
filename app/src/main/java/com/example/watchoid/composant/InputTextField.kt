package com.example.watchoid.composant

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputTextField(text: MutableState<String>, modifier: Modifier = Modifier){
    TextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        label = { Text("Enter value") },
        modifier = modifier
    )
}