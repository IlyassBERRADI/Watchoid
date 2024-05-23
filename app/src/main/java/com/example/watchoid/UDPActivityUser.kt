package com.example.watchoid

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.CheckBox
import com.example.watchoid.composant.DropDownMenu
import com.example.watchoid.composant.InputTextField
import com.example.watchoid.composant.InputTextFieldNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.nio.charset.Charset
import android.util.Log


class UDPActivityUser : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "UDP Test", main = false)
            UDP()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun UDP() {
    var sessionId by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, top = 80.dp)
    ) {
        // Ajouter des Spacer pour centrer verticalement la LazyColumn
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false) // Empêche LazyColumn de prendre tout l'espace
                    .padding(bottom = 100.dp) // Espace pour le bouton en bas
            ) {
                items(sessionId) { index ->
                    UDPTest()
                }
            }
        }

        // Bouton Ajouter un ICMP aligné en bas à droite
        Button(
            onClick = { sessionId += 1 },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))
        ) {
            Text("Ajouter un test")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun UDPTest() {
    var serverAddress = remember { mutableStateOf("") }
    var serverPort = remember { mutableStateOf("") }
    val period = remember { mutableStateOf("") }
    val expectedResult = remember { mutableStateListOf<ResponseComponent>() }
    val unitTime = remember { mutableStateOf("") }
    val value = remember { mutableStateOf("") }
    val charset = remember { mutableStateOf("") }
    val charsetList = listOf("ASCII", "UTF8")
    val result = remember { mutableStateOf("zoumiz") }
    val time = listOf("Secondes", "Minutes", "Heures", "Jours")
    val valueTypeList = listOf("Integer", "Long", "Float", "String", "Double")
    val valueType = remember { mutableStateOf("") }
    var coroutine = rememberCoroutineScope();
    val rows = remember { mutableStateListOf<RowComponent>() }
    val isChecked = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom // Aligner le contenu de la Row en bas
        ) {
            DropDownMenu(valueTypeList, valueType, modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp, end = 8.dp))
            if (valueType.value == "String"){
                InputTextField(text = value, modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp, end = 8.dp), label = "Value")
                DropDownMenu(charsetList, charset, modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp, end = 8.dp))
            } else {
                InputTextFieldNumber(text = value, modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp, end = 8.dp), label = "Value")
            }
        }
        if (valueType.value == "String"){
            CheckBox(isChecked, "Mettre la taille de la string dans le buffer")
        }
        Log.v("checked", isChecked.value.toString())
        // Dynamically added Rows
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom // Aligner le contenu de la Row en bas
            ) {
                DropDownMenu(valueTypeList, row.valueType, modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp, end = 8.dp))
                if (row.valueType.value == "String"){
                    InputTextField(text = row.period, modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, end = 8.dp), label = "Value")
                    DropDownMenu(charsetList, row.charset, modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, end = 8.dp))
                } else {
                    InputTextFieldNumber(text = row.period, modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp), label = "Value")
                }
            }
            if (row.valueType.value == "String"){
                CheckBox(row.isChecked, "Mettre la taille de la string dans le buffer")
            }
            Log.v("checked row", row.isChecked.value.toString())

        }

        Button(
            onClick = {
                rows.add(RowComponent())
            },shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A)),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Ajouter une ligne")
        }
        InputTextField(text = serverAddress, modifier = Modifier.padding(bottom = 8.dp), label = "Server's address")
        InputTextFieldNumber(text = serverPort, modifier = Modifier.padding(bottom = 8.dp), label = "Server's port")
        expectedResult.forEach { response ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom // Aligner le contenu de la Row en bas
            ) {
                DropDownMenu(valueTypeList, response.valueType, modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp, end = 8.dp))
                if (response.valueType.value == "String"){
                    InputTextField(text = response.value, modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, end = 8.dp), label = "Value")
                    DropDownMenu(charsetList, response.charset, modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, end = 8.dp))
                } else {
                    InputTextFieldNumber(text = response.value, modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp), label = "Value")
                }
            }
        }
        Button(
            onClick = {
                expectedResult.add(ResponseComponent())
            },shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A)),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Ajouter une résultat")
        }
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
//                if (result.value == expectedResult.value) {
//                    Text("Le test est passer !", Modifier.align(Alignment.Center))
//                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutine.launch(Dispatchers.IO) {
                    try {
                        val dc = DatagramChannel.open().bind(null)
                        var bb = ByteBuffer.allocate(1024)
                        val server: InetSocketAddress =
                            InetSocketAddress(serverAddress.value, serverPort.value.toInt())
                        when(valueType.value) {
                            "Float" -> bb.putFloat(value.value.toFloat())
                            "Double" -> bb.putDouble(value.value.toDouble())
                            "Long" -> bb.putLong(value.value.toLong())
                            "Integer" -> bb.putInt(value.value.toInt())
                            "String" -> {
                                if(isChecked.value){
                                    bb.putInt(value.value.length)
                                }
                                bb.put(Charset.forName(charset.value).encode(value.value))
                            }
                        }
                        rows.forEachIndexed { index, row ->
                            when(row.valueType.value) {
                                "Float" -> bb.putFloat(row.period.value.toFloat())
                                "Double" -> bb.putDouble(row.period.value.toDouble())
                                "Long" -> bb.putLong(row.period.value.toLong())
                                "Integer" -> bb.putInt(row.period.value.toInt())
                                "String" -> {
                                    if(row.isChecked.value){
                                        bb.putInt(row.period.value.length)
                                    }
                                    bb.put(Charset.forName(row.charset.value).encode(row.period.value))
                                }
                            }
                        }
                        bb.flip()
                        dc.send(bb, server)
                        bb.clear()
                        dc.receive(bb)
                        bb.flip()
                        val resultString = StringBuilder()
                        expectedResult.forEach { result ->
                            when(result.valueType.value) {
                                "Float" -> resultString.append(bb.getFloat())
                                "Double" -> resultString.append(bb.getDouble())
                                "Long" -> resultString.append(bb.getLong())
                                "Integer" -> resultString.append(bb.getInt())
                                "String" -> {
                                    // Lecture de la chaîne attendue
                                    val length = result.value.value.length
                                    val tmpbuffer = ByteBuffer.allocate(length)

                                    // Limiter le buffer à la taille de la chaîne attendue
                                    bb.limit(bb.position() + length)
                                    tmpbuffer.put(bb)
                                    tmpbuffer.flip()

                                    // Décoder les bytes en chaîne de caractères avec le charset spécifié
                                    val charset = Charset.forName(result.charset.value)
                                    val message = charset.decode(tmpbuffer).toString()
                                    resultString.append(message)

                                    // Réinitialiser la limite du buffer
                                    bb.limit(bb.capacity())
                                }
                            }
                        }
                        println(resultString.toString())
                        result.value = resultString.toString()
                    } catch (e: Exception) {
                    }
                }
            }
            , shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
            Text("Envoyer")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add some space before the divider
        Divider(color = Color.Gray, thickness = 2.dp) // Add a horizontal line
    }
    LaunchedEffect(serverAddress.value, value.value, period.value,unitTime.value,serverPort.value) {
        println("Je susi ")
        result.value = "zoumiz"
    }
}

