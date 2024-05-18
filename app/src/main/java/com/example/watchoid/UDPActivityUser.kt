package com.example.watchoid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.DropDownMenu
import com.example.watchoid.composant.InputTextField
import com.example.watchoid.composant.InputTextFieldNumber
import com.example.watchoid.ui.theme.WatchoidTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDPActivityUser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchoidTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    Background(text = "UDP Test", main = false)
                    var selectedType by remember { mutableStateOf("") }
                    var userText by remember { mutableStateOf("") }
                    DropdownMenuWithTextField(
                        onValueChanged = { selectedType = it }, // Fournir une fonction lambda vide pour onValueChanged
                        onTextChange = { userText = it }
                    )
                    UDPDemo(userText)
                }
            }
        }
    }
}

@Composable
fun ICMP() {
    var serverAddress = remember { mutableStateOf("") }
    var serverPort = remember { mutableStateOf("") }
    var timeout = remember { mutableStateOf("") }
    var ping by remember { mutableStateOf("zoumiz") }
    val packetSize = remember { mutableStateOf("") }
    val period = remember { mutableStateOf("") }
    val expectedResult = remember { mutableStateOf("") }
    val expectedResultList = listOf("true", "false")
    val unitTime = remember { mutableStateOf("") }
    val time = listOf("Secondes", "Minutes", "Heures", "Jours")
    var coroutine = rememberCoroutineScope();
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
    ) {
        InputTextField(text = serverAddress, modifier = Modifier.padding(bottom = 8.dp), label = "Server's address")
        InputTextFieldNumber(text = serverPort, modifier = Modifier.padding(bottom = 8.dp), label = "Server's port")
        InputTextFieldNumber(text = timeout, modifier = Modifier.padding(bottom = 8.dp), label = "Timeout")
        InputTextFieldNumber(text = packetSize, modifier = Modifier.padding(bottom = 8.dp), label = "ICMP Packet Size")
        DropDownMenu(expectedResultList, expectedResult, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom // Aligner le contenu de la Row en bas
        ) {
            InputTextFieldNumber(text = period, modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp), label = "Périodicité")
            DropDownMenu(time, unitTime, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)){
                if (ping == expectedResult.value) {
                    Text("Le serveur est passer !", Modifier.align(Alignment.Center))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutine.launch(Dispatchers.IO) {

                    try {
                        val address = InetAddress.getByName(serverAddress.value)
                        val socket = DatagramSocket()

                        // Set the timeout for the socket
                        socket.soTimeout = timeout.value.toIntOrNull()!!

                        // Prepare the ICMP packet
                        val data = ByteArray(packetSize.value.toIntOrNull()!!)
                        val packet = DatagramPacket(data, data.size, address, serverPort.value.toIntOrNull() ?: 0)

                        // Send the packet
                        socket.send(packet)

                        // On attend la réponse
                        val receiveData = ByteArray(1024)
                        val receivePacket = DatagramPacket(receiveData, receiveData.size)
                        socket.receive(receivePacket)

                        // Close the socket
                        socket.close()
                        Log.i("value", expectedResult.value)
                        ping = "true";
                        Log.i("ping", ping)

                    } catch (e: Exception) {
                        ping = "false"
                    }
                }
            }
            , shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
            Text("Envoyer")
        }
    }
    LaunchedEffect(expectedResult, serverAddress, serverPort, timeout, packetSize ) {
        ping = "zoumiz"
    }
}

@Composable
fun UDPDemo(messageToSend: String) {
    var serverAddress by remember { mutableStateOf("") }
    var serverPort by remember { mutableStateOf("") }
    var snackbarVisible by remember { mutableStateOf(false) }
    var reponseDuServeur by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
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
                    Log.d("MessaageToSend", "MessageToSend : $messageToSend")
                    Log.d("adresseServeur", "adresseServeur : $serverAddress")
                    Log.d("port", "port : $serverPort")

                    Thread {
                        try {
                            val socket = DatagramSocket()
                            val adresse = InetAddress.getByName(serverAddress)
                            val data = messageToSend.toByteArray()
                            val packetEnvoye = DatagramPacket(data, data.size, adresse, serverPort.toIntOrNull() ?: 0)
                            Log.d("CreatePacket", "CreatePacket done")
                            socket.send(packetEnvoye)
                            Log.d("send", "Packet send")

                            // Préparer le paquet pour recevoir la réponse
                            val buffer = ByteArray(1024)
                            val packetRecu = DatagramPacket(buffer, buffer.size)
                            // Attendre la réponse du serveur
                            socket.receive(packetRecu)
                            // Convertir les données reçues en chaîne de caractères
                            reponseDuServeur = String(packetRecu.data, 0, packetRecu.length)
                            // Afficher la réponse du serveur avec un logger
                            Log.d("ReponseServeur", "Réponse du serveur : $reponseDuServeur")

                            socket.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }.start()
                snackbarVisible = true
            }
            , shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
            Text("Envoyer")
        }
    }
    if (snackbarVisible) {
        Snackbar(
            action = {
                TextButton(onClick = { snackbarVisible = false }) {
                    Text("OK")
                }
            }
        ) {
            Text("Réponse du serveur "+reponseDuServeur)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithTextField(onValueChanged: (String) -> Unit, onTextChange: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("") }
    val textFieldValue = remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier.fillMaxSize().padding(bottom = 200.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        // Zone de texte
        OutlinedTextField(
            value = textFieldValue.value,
            onValueChange = {
                textFieldValue.value = it
                onTextChange(it.text) // Appel de la fonction de rappel avec la nouvelle valeur du texte
            },
            label = { Text("Entrez votre texte ici") },
        )

        // Menu déroulant
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }
        ) {

            TextField(
                value = type,
                onValueChange = {
                    type = it
                    onValueChanged(it) // Appeler la fonction de rappel avec la nouvelle valeur
                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Float")
                    },
                    onClick = {
                        type = "Float"
                        isExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "Int")
                    },
                    onClick = {
                        type = "Int"
                        isExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "String")
                    },
                    onClick = {
                        type = "String"
                        isExpanded = false
                    }
                )
            }
        }
    }
}