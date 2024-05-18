package com.example.watchoid.composant

import android.content.Intent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.reflect.KClass

@Composable
fun NavigationButton(text: String, destinationClass: KClass<*>, modifier: Modifier = Modifier){
    val context = LocalContext.current
    Button(onClick = {
        context.startActivity(Intent(context, destinationClass.java))
    }, shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
        Text(text = text, fontSize = 20.sp)
    }
}