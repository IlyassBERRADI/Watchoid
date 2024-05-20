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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.watchoid.composant.Background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress

class TCPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            TCPTest()
        }
    }

    @Composable
    fun TCPTest() {
        var snackbarVisible by remember { mutableStateOf(false) }
        var reponseDuServeur by remember { mutableStateOf("") }
        var serverAddress by remember { mutableStateOf("www.google.fr") }
        var serverPort by remember { mutableStateOf("80") }
        val coroutineScope = rememberCoroutineScope()
        val state = rememberScrollState()
        val state2 = rememberScrollState()
        var request by remember { mutableStateOf("") }
        var response by remember { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("") }
        var serverLaunched by remember { mutableStateOf(false) }
        var data by remember { mutableStateOf<String?>(null) }
        var ping by remember { mutableStateOf(false) }
        /*LaunchedEffect(serverLaunched) {
            if (serverLaunched){
                withContext(IO) {
                    TCPServer(serverPort.toIntOrNull()).launch()
                }
            }
        }*/

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state)
            , horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {
            Background(text = "TCP Test", main = false)
            Spacer(modifier = Modifier.height(30.dp))
            DropdownMenuWithTextField(onValueChanged = { selectedType = it }, // Fournir une fonction lambda vide pour onValueChanged
                onTextChange = { request = it })
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Réponse :", modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 28.dp))
            Box(
                //contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
                    .verticalScroll(state2)
                    .background(Color.White), // Set the background color to white

            ){
                Text(text = response)
            }


            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = serverAddress,
                onValueChange = { serverAddress = it },
                label = { Text("Server Address") }
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = serverPort
                , onValueChange = { serverPort = it },
                label = { Text("Server Port") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (!serverLaunched)
                        serverLaunched=true
                    coroutineScope.launch(IO) {
                        var server = InetSocketAddress(serverAddress, serverPort.toInt())
                        response=TCPClient.getResponse(request, server)
                    }

                    /*Thread {
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
                    }.start()*/
                }
                , shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }
        }
    }
}
