package com.example.watchoid.composant

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.Alert
import com.example.watchoid.R
import com.example.watchoid.Settings
import com.example.watchoid.TapTap

@Composable
fun Background(text: String, main: Boolean,modifier: Modifier = Modifier){
    val context = LocalContext.current
    var clickCount by remember { mutableStateOf(0) }
    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp).shadow( // Ajouter une ombre
            elevation = 8.dp, // Taille de l'ombre
            shape = RectangleShape, // Forme de l'ombre
            clip = true // Découpe le contenu à la forme de l'ombre
        ).background(color = Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LaunchedEffect(clickCount) {
            if (clickCount > 0) {
                kotlinx.coroutines.delay(1000) // 1 second delay
                clickCount = 0
            }
        }
        if (main) {
            Image(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Image 1",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        context.startActivity(Intent(context, Alert::class.java))
                    }
            )
        }
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterVertically).
            clickable {
                clickCount++
                if (clickCount == 3) {
                    context.startActivity(Intent(context, TapTap::class.java))
                }
            }
        )
        if (main) {
            Image(
                painter = painterResource(id = R.drawable.parametres),
                contentDescription = "Image 2",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        context.startActivity(Intent(context, Settings::class.java))
                    }
            )
        }
    }
}