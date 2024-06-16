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
import android.util.Log
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
import androidx.compose.runtime.collectAsState
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
import com.example.watchoid.service.TestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
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


    @Composable
    fun TCPTest() {
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
        var result by remember {
            mutableStateOf(false)
        }
        var resultText by remember {
            mutableStateOf("")
        }
        var typeBufferResponse by remember { mutableStateOf("") }
        var closeInput by remember { mutableStateOf(false) }
        var envoi by remember { mutableStateOf(false) }
        var selectedEncoding by remember { mutableStateOf("") }
        var selectedEncoding2 by remember { mutableStateOf("") }
        var waitedResponse by remember {
            mutableStateOf("")
        }

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
                    .background(Color.White),

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
                    envoi = false

                    coroutineScope.launch(IO) {

                        if (isConnectedToNetwork(context)){
                            var server = InetSocketAddress(serverAddress, serverPort.toInt())
                            var exceptionCaught = false
                            var exceptionCaught2 = false

                            try {
                                response=TCPClient.getResponse(byteBuffer, server, closeInput, typeBufferResponse, sizeBufferResponse)
                            } catch (e : IOException){
                                exceptionCaught2 = true
                            } catch (e : UnresolvedAddressException){
                                exceptionCaught = true
                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "Adresse non connu!", Toast.LENGTH_SHORT).show()
                                }
                            }
                            if (!exceptionCaught){
                                if (!exceptionCaught2 && Regex(pattern = waitedResponse).containsMatchIn(response)){
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
                                var idTest = MainActivity.database.tcpTest().insert(test)
                                var alert = Alerts(idTest = idTest.toInt(), testType = "TCP", nbError = 0 )
                                MainActivity.database.alerts().insert(alert)

                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "Test enregistré", Toast.LENGTH_SHORT).show()
                                }

                            }
                            else{
                                Toast.makeText(context, "Aucune connexion réseau!!", Toast.LENGTH_SHORT).show()}
                        }
                    }
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
            var result : Boolean
            var context = ctx
            var error = false
            val service = AlertNotificationService(context)
            var response : String = ""
            val query = selectAllFrom("tcp_tests")
            var tests = MainActivity.database.tcpTest().getAllTests(query)
            var period : Long = MainActivity.database.settingsTable().getSettingByProtocol("TCP")?.periodicity?.toLong()
                ?: 0
            var periodicityUnit = MainActivity.database.settingsTable().getSettingByProtocol("TCP")?.timeUnitPeriodicity
            when(periodicityUnit){
                "Min" -> period *= 60
                "Heure" -> period *= 60*60
                "Jour" -> period *= 24*60*60
            }
            while (true){
                for (test in tests){
                    if (isConnectedToNetwork(context)){
                        var byteBuffer = ByteBuffer.allocate(1024)
                        var server = InetSocketAddress(test.dstIp, test.dstPort)

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
                        }

                        var log = com.example.watchoid.entity.Log(idTest = test.idTest, date = LocalDateTime.now().toString(), testType = "TCP", result = result)
                        MainActivity.database.log().insert(log)
                        var firstAlert = MainActivity.database.alerts().getAlertByTestId(test.idTest, "TCP")
                        var alert : Alerts
                        if (!error && !result) {
                            if (firstAlert != null) {
                                alert = Alerts(id_alert = firstAlert.id_alert, idTest = test.idTest, testType = "TCP", nbError = (firstAlert?.nbError
                                    ?: 0) +1)
                                MainActivity.database.alerts().update(alert)
                                if (alert.nbError==MainActivity.database.settingsTable().getNbAlertByProtocol("TCP")) {
                                    service.showNotificationAlert(test.idTest, "TCP")
                                    error = true
                                }
                            }
                        }
                        else if (error && result){
                            service.showNotificationAlert2(test.idTest, "TCP")
                            if (firstAlert != null) {
                                alert = Alerts(id_alert = firstAlert.id_alert, idTest = test.idTest, testType = "TCP", nbError = 0)
                                MainActivity.database.alerts().update(alert)
                            }
                            error = false
                        }
                    }
                    else{
                        service.showNotificationInternet()
                        val intent : Intent = Intent(context, TestService::class.java)
                        intent.action = TestService.Actions.STOP.toString()
                        context.startService(intent)
                        return

                        }

                }

                delay(period*1000)
            }



        }

        fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")

        @Throws(IOException::class)
        private fun readFully(socketChannel : SocketChannel, buffer : ByteBuffer) : Boolean{
            while (socketChannel.read(buffer)!=-1){
                if (!buffer.hasRemaining()){
                    return true
                }
            }
            return  false
        }


        fun isConnectedToNetwork(ctx : Context) : Boolean{
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val transport = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                val transport2 = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                return network!=null && capabilities!=null && (transport == true || transport2 == true)
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo!=null && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
            }
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

        var expanded by remember { mutableStateOf(false) }

        var selectedText by remember { mutableStateOf("") }

        var textFieldSize by remember { mutableStateOf(Size.Zero)}

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Column(Modifier.padding(20.dp)) {

            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                label = {Text(label)},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )

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

