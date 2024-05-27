package com.example.watchoid.composant

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckBox(isChecked: MutableState<Boolean>, text: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = it }
            )

            // Text associé à la checkbox
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text)
        }
}