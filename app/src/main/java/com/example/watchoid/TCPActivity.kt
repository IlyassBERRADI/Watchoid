package com.example.watchoid

import android.os.Bundle
import android.widget.Toast
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.watchoid.composant.Background
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

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
        var serverAddress by remember { mutableStateOf("") }
        var byteBuffer by remember { mutableStateOf<ByteBuffer>(ByteBuffer.allocate(1024)) }
        var serverPort by remember { mutableStateOf("") }
        var sizeBufferResponse: Int? by remember { mutableStateOf(null) }
        val coroutineScope = rememberCoroutineScope()
        val state = rememberScrollState()
        val state2 = rememberScrollState()
        var empty by remember { mutableStateOf(false) }
        var addSize by remember { mutableStateOf(false) }
        var request by remember { mutableStateOf("") }
        var response by remember { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("") }
        var selectedType2 by remember { mutableStateOf("") }
        var serverLaunched by remember { mutableStateOf(false) }
        var listTypes by remember { mutableStateOf(mutableListOf<String>()) }
        var typeBufferResponse by remember { mutableStateOf("") }
        var closeInput by remember { mutableStateOf(false) }
        //var sizeResponse by remember { mutableIntStateOf(-1) }
        var envoi by remember { mutableStateOf(false) }
        var selectedEncoding by remember { mutableStateOf("") }
        var selectedEncoding2 by remember { mutableStateOf("") }
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
            val context = LocalContext.current
            Background(text = "TCP Test", main = false)
            Spacer(modifier = Modifier.height(30.dp))
            DropdownMenuWithTextField(
                listOf("Double", "Long", "Int", "String"),
                label = "Entrez votre texte ici",
                onValueChanged = { selectedType = it }, // Fournir une fonction lambda vide pour onValueChanged
                onTextChange = { request = it },
                empty = empty,
                onEmptyChange = {empty = it})
            Spacer(modifier = Modifier.height(20.dp))
            MyCheckBox("Mettre la taille avant la chaîne encodée") { addSize = it }
            MyCheckBox("Fermer l'input à la fin") { closeInput = it }
            MyDropDownMenu("Encodage", listOf("UTF-8", "ASCII", "ISO-8859-1")) { selectedEncoding = it }
            Button(onClick = {
                if (byteBuffer.remaining() <Double.SIZE_BYTES) {
                    byteBuffer.flip()
                    var buffer = ByteBuffer.allocate(byteBuffer.capacity()*2).put(byteBuffer)
                    byteBuffer = buffer
                    byteBuffer.compact()
                }
                when(selectedType){
                    "Double" -> {
                        byteBuffer.putDouble(request.toDouble())
                    }
                    "Long" -> {
                        byteBuffer.putLong(request.toLong())
                    }
                    "Int" -> {
                        byteBuffer.putInt(request.toInt())
                    }
                    "String" -> {
                        var stringBuffer = Charset.forName(selectedEncoding).encode(request)
                        if (addSize){
                            byteBuffer.putInt(stringBuffer.remaining())
                        }
                        while (byteBuffer.remaining() <stringBuffer.remaining()) {
                            byteBuffer.flip()
                            var buffer = ByteBuffer.allocate(byteBuffer.capacity()*2).put(byteBuffer)
                            byteBuffer = buffer

                        }
                        byteBuffer.put(stringBuffer)
                    }
                }
                empty = true
            }) {
                Text("Ajouter au buffer d'envoi")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Lecture de buffer réponse", modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 28.dp),
                fontSize = 15.sp)
            Text("Ajouter le type pour définir le format du buffer réponse", modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 28.dp),
                fontSize = 13.sp)
            /*OutlinedTextField(
                value = if(sizeResponse==-1) "" else sizeResponse.toString(),
                onValueChange = { sizeResponse = it.toInt() },
                label = { Text("Taille") }
            )*/
            MyDropDownMenu("Type de réponse", listOf("Double", "Long", "Int", "String")) { selectedType2 = it }
            MyDropDownMenu("Encodage", listOf("UTF-8", "ASCII", "ISO-8859-1")) { selectedEncoding2 = it }
            OutlinedTextField(
                value = if(sizeBufferResponse==null) "" else sizeBufferResponse.toString(),
                onValueChange = { sizeBufferResponse = if(it=="") null else it.toInt()   },
                label = { Text("Size") }
            )
            Button(onClick = {
                if (selectedType2 == "String"){
                   typeBufferResponse = selectedEncoding2
                }
                else
                    typeBufferResponse = selectedType2
                envoi = true
                Toast.makeText(context, "Type ajouté", Toast.LENGTH_SHORT).show()
            }) {
                Text("Ajouter au buffer de réponse")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Réponse :", modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 28.dp))
            Box(
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
                    envoi = false

                    coroutineScope.launch(IO) {
                        TCPServer(7777).launch()
                    }

                    coroutineScope.launch(IO) {
                        var server = InetSocketAddress(serverAddress, serverPort.toInt())
                        //"www.google.fr", 80
                        /*var server = InetSocketAddress("www.google.fr", 80)
                        Log.i("closeIput", closeInput.toString())*/
                        response=TCPClient.getResponse(byteBuffer, server, closeInput, typeBufferResponse, sizeBufferResponse)
                        //response=TCPClientWeb.getResponse("GET / HTTP/1.1\\r\\nHost: www.google.fr\\r\\n\\r\\n", server)
                        //Log.i("response", response)
                        //var server = InetSocketAddress("www.google.fr", 80)
                        //Log.i("request", request)
                        //response=TCPClientWeb.getResponse(request, server)
                        //Log.i("text", text)
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
                enabled = envoi,
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }
        }
    }


    @Composable
    fun MyCheckBox(text : String, onValueChange : (Boolean) -> Unit){
        var checked by rememberSaveable { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onValueChange(it)
                }
            )
            Text(
                text
            )
        }
    }

    @Composable
    fun MyDropDownMenu(label : String, list : List<String>, onValueChange: (String) -> Unit){

        // Declaring a boolean value to store
        // the expanded state of the Text Field
        var expanded by remember { mutableStateOf(false) }
        // Create a list of cities

        // Create a string value to store the selected city
        var selectedText by remember { mutableStateOf("") }

        var textFieldSize by remember { mutableStateOf(Size.Zero)}

        // Up Icon when expanded and down icon when collapsed
        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(20.dp)) {

            // Create an Outlined Text Field
            // with icon and not expanded
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        // This value is used to assign to
                        // the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    },
                label = {Text(label)},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )

            // Create a drop-down menu with list of cities,
            // when clicked, set the Text Field text as the city selected
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){textFieldSize.width.toDp()})
            ) {
                list.forEach { label ->
                    DropdownMenuItem(
                        text = {
                            Text(text = label)
                        },
                        onClick = {
                            selectedText = label
                            expanded = false
                            onValueChange(label)
                        })
                }
            }
        }
    }
}
