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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.composant.Background
import com.example.watchoid.ui.theme.WatchoidTheme
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class TPCActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "TCP Test", main = false)
            TCPTest()
        }
    }

    @Composable
    fun TCPTest() {
        var serverAddress = "10.0.2.2"
        var serverPort = 7777
        var ping by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Hauteur du rectangle
                .background(color = Color.White) // Couleur du rectangle
        ) {
            if (ping) {
                Text(
                    text = "Server has been reached !\n Test passed ",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Text(
                    text = "Server is disconnected !\n Test not passed ",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    /*coroutineScope.launch(IO) {
                        var server = InetSocketAddress( 7777)
                        text=TCPClient.getResponse("abc", server)
                    }*/
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
                            val packet = DatagramPacket(data, data.size, address, serverPort)

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
                , shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }
        }
    }
}
