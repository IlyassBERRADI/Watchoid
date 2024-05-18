package com.example.watchoid.composant

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.core.content.ContextCompat.startActivity
import com.example.watchoid.Alert
import com.example.watchoid.R
import com.example.watchoid.Settings

@Composable
fun Background(text: String, main: Boolean,modifier: Modifier = Modifier){
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp) // Hauteur du rectangle
                .align(Alignment.TopCenter) // Alignement en haut
                .shadow( // Ajouter une ombre
                    elevation = 8.dp, // Taille de l'ombre
                    shape = RectangleShape, // Forme de l'ombre
                    clip = true // Découpe le contenu à la forme de l'ombre
                )
                .background(color = Color.LightGray), // Couleur du rectangle
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(main){
                    Image(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Image 1",
                        modifier = Modifier.size(32.dp).clickable {
                            context.startActivity(Intent(context, Alert::class.java))
                        }
                    )
                }
                Text(
                    text = text,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                if (main){
                    Image(
                        painter = painterResource(id = R.drawable.parametres),
                        contentDescription = "Image 2",
                        modifier = Modifier.size(32.dp).clickable {
                            context.startActivity(Intent(context, Settings::class.java))
                        }
                    )
                }
            }
        }
    }
}