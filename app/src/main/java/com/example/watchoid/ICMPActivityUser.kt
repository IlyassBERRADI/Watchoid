package com.example.watchoid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ICMPActivityUser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            backgroundICMPU()
            ICMPDemo()
        }
    }

    @Composable
    fun backgroundICMPU(){
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
                Text(text = "UDP Test",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
    @Composable
    fun ICMPDemo() {
        var serverAddress by remember { mutableStateOf("") }
        var serverPort by remember { mutableStateOf("") }
        var ping by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {
            OutlinedTextField(
                value = serverAddress,
                onValueChange = { serverAddress = it },
                label = { Text("Server Address") }
            )
            OutlinedTextField(
                value = serverPort,
                onValueChange = { serverPort = it },
                label = { Text("Server Port") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {

                    Thread {
                        val timeout = 5000 // 5 seconds
                        val packetSize = 64 // ICMP packet size

                        try {
                            val address = InetAddress.getByName(serverAddress)
                            val socket = DatagramSocket()

                            // Set the timeout for the socket
                            socket.soTimeout = timeout

                            // Prepare the ICMP packet
                            val data = ByteArray(packetSize)
                            val packet = DatagramPacket(data, data.size, address, serverPort.toIntOrNull() ?: 0)

                            // Send the packet
                            socket.send(packet)
                            println("Ping sent to $serverAddress")

                            // On attend la réponse
                            val receiveData = ByteArray(1024)
                            val receivePacket = DatagramPacket(receiveData, receiveData.size)
                            socket.receive(receivePacket)

                            // Close the socket
                            socket.close()

                            ping = true;
                        } catch (e: Exception) {
                            println("Error sending ping: ${e.message}")
                            ping = false
                        }
                    }.start()
                }
            ) {
                Text("Envoyer")
            }
        }

        if (ping) {
            Snackbar(
                action = {
                    TextButton(onClick = { ping = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text("Serveur est lààà !!")
            }
        }
    }
}
