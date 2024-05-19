package com.example.watchoid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.NavigationButton

class Protocol_chooser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "New Test", main = false)
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationButton("UDP", UDPActivityUser::class)
                NavigationButton("TCP", TCPActivity::class)
                NavigationButton("ICMP", ICMPActivity::class)
                NavigationButton("HTTP", HTTPActivity::class)
            }
        }
    }
}


