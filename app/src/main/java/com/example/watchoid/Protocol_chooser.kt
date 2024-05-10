package com.example.watchoid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Protocol_chooser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            backgroundProtocol()
            
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    val navigate = Intent(this@Protocol_chooser, UDPActivityUser::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "UDP", fontSize = 20.sp)
                }
                Button(onClick = {
                    val navigate = Intent(this@Protocol_chooser, TPCActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "TCP", fontSize = 20.sp)
                }
                Button(onClick = {
                    val navigate = Intent(this@Protocol_chooser, ICMPActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "ICMP", fontSize = 20.sp)
                }
                Button(onClick = {
                    val navigate = Intent(this@Protocol_chooser, HTTPActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "HTTP", fontSize = 20.sp)
                }
            }
        }
    }

    @Composable
    fun backgroundProtocol(){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Hauteur du rectangle
                    .align(Alignment.TopCenter) // Alignement en haut
                    .shadow( // Ajouter une ombre
                        elevation = 8.dp, // Taille de l'ombre
                        shape = RectangleShape, // Forme de l'ombre
                        clip = true // Découpe le contenu à la forme de l'ombre
                    )
                    .background(color = Color.LightGray) // Couleur du rectangle
            ) {
                Text(text = "New Test",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


