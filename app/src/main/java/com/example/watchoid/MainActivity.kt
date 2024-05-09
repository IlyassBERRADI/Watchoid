package com.example.watchoid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            background()
            
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    val navigate = Intent(this@MainActivity, UDPActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "UDP", fontSize = 20.sp)
                }
                Button(onClick = {
                    val navigate = Intent(this@MainActivity, UDPActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "UDP", fontSize = 20.sp)
                }
                Button(onClick = {
                    val navigate = Intent(this@MainActivity, ICMPActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "ICMP", fontSize = 20.sp)
                }
                Button(onClick = {
                    val navigate = Intent(this@MainActivity, UDPActivity::class.java)
                    startActivity(navigate)
                }) {
                    Text(text = "HTTP", fontSize = 20.sp)
                }
            }
        }
    }
}


