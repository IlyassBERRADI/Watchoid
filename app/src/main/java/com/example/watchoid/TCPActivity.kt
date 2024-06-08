package com.example.watchoid

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.watchoid.composant.Background
import com.example.watchoid.entity.Alerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.channels.UnresolvedAddressException
import java.nio.charset.Charset
import java.time.LocalDateTime


class TCPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            TCPTest()
        }
    }

    fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")


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
        var connectedToNetwork by remember { mutableStateOf(false) }
        var request by remember { mutableStateOf("") }
        var response by remember { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("") }
        var selectedType2 by remember { mutableStateOf("") }
        var result by remember {
            mutableStateOf(false)
        }
        var resultText by remember {
            mutableStateOf("")
        }
        //var serverLaunched by remember { mutableStateOf(false) }
        var listTypes by remember { mutableStateOf(mutableListOf<String>()) }
        var typeBufferResponse by remember { mutableStateOf("") }
        var closeInput by remember { mutableStateOf(false) }
        //var sizeResponse by remember { mutableIntStateOf(-1) }
        var envoi by remember { mutableStateOf(false) }
        var selectedEncoding by remember { mutableStateOf("") }
        var selectedEncoding2 by remember { mutableStateOf("") }
        var waitedResponse by remember {
            mutableStateOf("")
        }

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
                //empty = false,
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
                //empty = true
                Toast.makeText(context, "Texte ajouté au buffer", Toast.LENGTH_SHORT).show()
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

                Toast.makeText(context, "Type ajouté", Toast.LENGTH_SHORT).show()
            }) {
                Text("Ajouter au buffer de réponse")
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = waitedResponse,
                onValueChange = { waitedResponse = it  },
                label = { Text("Réponse attendu") }
            )
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
            Text(text = result.toString())
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = serverAddress,
                onValueChange = { serverAddress = it },
                label = { Text("Server Address") }
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = serverPort
                , onValueChange = {
                    serverPort = it
                    envoi = true },
                label = { Text("Server Port") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    /*if (!serverLaunched)
                        serverLaunched=true*/
                    envoi = false

                    /*coroutineScope.launch(IO) {
                        TCPServer(7777).launch()
                    }*/
                    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val network = connectivityManager.activeNetwork
                        val capabilities = connectivityManager.getNetworkCapabilities(network)
                        val transport = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        val transport2 = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        connectedToNetwork = if (network!=null && capabilities!=null && (transport == true || transport2 == true)){
                            true
                        } else
                            false
                    } else {
                        val networkInfo = connectivityManager.activeNetworkInfo
                        connectedToNetwork = if (networkInfo!=null && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE))
                            true
                        else
                            false
                    }

                    if (connectedToNetwork){
                        coroutineScope.launch(IO) {
                            var server = InetSocketAddress(serverAddress, serverPort.toInt())
                            var exceptionCaught = false
                            //"www.google.fr", 80
                            /*var server = InetSocketAddress("www.google.fr", 80)
                            Log.i("closeIput", closeInput.toString())*/

                            try {
                                response=TCPClient.getResponse(byteBuffer, server, closeInput, typeBufferResponse, sizeBufferResponse)
                            } catch (e : IOException){
                                result = false
                                resultText = "Echec de test"
                            } catch (e : UnresolvedAddressException){
                                exceptionCaught = true
                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "Adresse non connu!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            if (!exceptionCaught){
                                if (Regex(pattern = waitedResponse).containsMatchIn(response)){
                                    result = true
                                    resultText = "Succès du test"
                                }
                                else{
                                    result = false
                                    resultText = "Échec du test"
                                }

                                var test = com.example.watchoid.entity.TCPTest(request = request, typeRequest = selectedType, dstIp = serverAddress, resultExpected = waitedResponse ,
                                    sizeBeforeString = addSize, closeInput = closeInput, inEncoding = selectedEncoding, typeResponse = selectedType2, outEncoding = selectedEncoding2,
                                    sizeResponse = sizeBufferResponse, dstPort = serverPort.toInt())
                                MainActivity.database.tcpTest().insert(test)
                                val query = selectAllFrom("tcp_tests")
                                var id = MainActivity.database.tcpTest()

                                var list = id.getAllTests(query)
                                //var alert = Alerts(idTest = list.size, testType = "TCP", nbError = 0)
                                //MainActivity.database.alerts().insert(alert)
                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "Test enregistré", Toast.LENGTH_SHORT).show()
                                }

                            }


                            //response=TCPClientWeb.getResponse("GET / HTTP/1.1\\r\\nHost: www.google.fr\\r\\n\\r\\n", server)
                            //Log.i("response", response)
                            //var server = InetSocketAddress("www.google.fr", 80)
                            //Log.i("request", request)
                            //response=TCPClientWeb.getResponse(request, server)
                            //Log.i("text", text)
                        }

                    }

                    else
                        Toast.makeText(context, "Aucune connexion réseau!!", Toast.LENGTH_SHORT).show()

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

    companion object {
        suspend fun automaticTCPTest(ctx : Context) {

            var isStopped : Boolean
            var result : Boolean
            var resultText : String
            var connectedToNetwork : Boolean
            var context = ctx//LocalContext.current
            var response : String = ""
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val transport = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                val transport2 = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                connectedToNetwork = network!=null && capabilities!=null && (transport == true || transport2 == true)
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                connectedToNetwork = networkInfo!=null && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
            }
            val query = selectAllFrom("tcp_tests")
            var tests = MainActivity.database.tcpTest().getAllTests(query)
            var period : Long = MainActivity.database.settingsTable().getSettingByProtocol("TCP")?.periodicity?.toLong()
                ?: 0
            while (true){
                for (test in tests){
                    if (connectedToNetwork){
                        var byteBuffer = ByteBuffer.allocate(1024)
                        var server = InetSocketAddress(test.dstIp, test.dstPort)
                        var exceptionCaught = false

                        if (byteBuffer.remaining() <Double.SIZE_BYTES) {
                            byteBuffer.flip()
                            var buffer = ByteBuffer.allocate(byteBuffer.capacity()*2).put(byteBuffer)
                            byteBuffer = buffer
                            byteBuffer.compact()
                        }
                        when(test.typeRequest){
                            "Double" -> {
                                byteBuffer.putDouble(test.request.toDouble())
                            }
                            "Long" -> {
                                byteBuffer.putLong(test.request.toLong())
                            }
                            "Int" -> {
                                byteBuffer.putInt(test.request.toInt())
                            }
                            "String" -> {
                                var stringBuffer = Charset.forName(test.inEncoding).encode(test.request)
                                if (test.sizeBeforeString){
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


                        try {
                            response=TCPClient.getResponse(byteBuffer, server, test.closeInput, test.outEncoding, test.sizeResponse)
                            result = Regex(pattern = test.resultExpected).containsMatchIn(response)
                        } catch (e : IOException){
                            result = false
                        } /*catch (e : UnresolvedAddressException){
                        exceptionCaught = true
                    }*/


                        var log = com.example.watchoid.entity.Log(idTest = test.idTest, date = LocalDateTime.now().toString(), testType = "TCP", result = result)
                        MainActivity.database.log().insert(log)
                        var firstAlert = MainActivity.database.alerts().getAlertByTestId(test.idTest)
                        var alert : Alerts
                        if (!result) {
                            alert = Alerts(idTest = test.idTest, testType = "TCP", nbError = (firstAlert?.nbError
                                ?: 0) +1)
                            if (alert.nbError==MainActivity.database.settingsTable().getNbAlertByProtocol("TCP"))
                                showNotification(context, "New Alert", "TCP Test Failed"+ alert.nbError+ " times")
                        }

                        //MainActivity.database.alerts().insert(alert)
                        //Toast.makeText(context, "Test enregistré", Toast.LENGTH_SHORT).show()



                        //response=TCPClientWeb.getResponse("GET / HTTP/1.1\\r\\nHost: www.google.fr\\r\\n\\r\\n", server)
                        //Log.i("response", response)
                        //var server = InetSocketAddress("www.google.fr", 80)
                        //Log.i("request", request)
                        //response=TCPClientWeb.getResponse(request, server)
                        //Log.i("text", text)


                    }

                    else
                        showNotification(context, "New Alert", "No internet connection!")
                }

                delay(period*1000)
            }



        }

        private fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")

        @Throws(IOException::class)
        private fun readFully(socketChannel : SocketChannel, buffer : ByteBuffer) : Boolean{
            while (socketChannel.read(buffer)!=-1){
                if (!buffer.hasRemaining()){
                    return true
                }
            }
            return  false
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



fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "TCP Test Notifications"
        val descriptionText = "Notifications for TCP test results"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("tcp_test_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showNotification(context: Context, title: String, message: String) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, "tcp_test_channel")
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@with
        }
        notify(2, builder.build())
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

