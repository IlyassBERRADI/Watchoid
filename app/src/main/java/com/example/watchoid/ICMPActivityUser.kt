package com.example.watchoid

import android.R.attr.host
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.DropDownMenu
import com.example.watchoid.composant.InputTextField
import com.example.watchoid.composant.InputTextFieldNumber
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress


class ICMPActivityUser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "ICMP Test", main = false)
            ICMPDemo()
        }
    }

    fun extractPingStats(pingOutput: String): Map<String, String> {
        val regexPacket = """(\d+)% packet loss""".toRegex()
        val packetLoss = regexPacket.find(pingOutput)
        val regex = """rtt min/avg/max/mdev = ([\d.]+)/([\d.]+)/([\d.]+)/([\d.]+) ms""".toRegex()
        val matchResult = regex.find(pingOutput)

        return if (matchResult != null && packetLoss != null) {
            mapOf(
                "min" to matchResult.groupValues[1],
                "avg" to matchResult.groupValues[2],
                "max" to matchResult.groupValues[3],
                "mdev" to matchResult.groupValues[4],
                "packetLoss" to packetLoss.groupValues[1]
            )
        } else {
            emptyMap()
        }
    }

    fun pingMaxTime(nbPacket: MutableState<String>, serverAddress: MutableState<String>): Double? {
        val command = "ping -c ${nbPacket.value} ${serverAddress.value}"
        val process = Runtime.getRuntime().exec(command)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val result = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null){
            result.append(line).append("\n")
        }

        process.waitFor()
        println(result.toString())
        val pingStats = extractPingStats(result.toString())
        if (pingStats.isNotEmpty()) {
            println("Min: ${pingStats["min"]} ms")
            println("Avg: ${pingStats["avg"]} ms")
            println("Max: ${pingStats["max"]} ms")
            println("Mdev: ${pingStats["mdev"]} ms")
            return pingStats["max"]?.toDoubleOrNull()
        } else {
            println("Impossible de trouver les statistiques de ping dans la sortie.")
            return 0.0
        }
    }

    fun ping(nbPacket: MutableState<String>, serverAddress: MutableState<String>):Int? {
        val command = "ping -c ${nbPacket.value} ${serverAddress.value}"
        val process = Runtime.getRuntime().exec(command)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val result = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null){
            result.append(line).append("\n")
        }

        process.waitFor()
        println(result.toString())
        val pingStats = extractPingStats(result.toString())
        if (pingStats.isNotEmpty()) {
            println("Min: ${pingStats["min"]} ms")
            println("Avg: ${pingStats["avg"]} ms")
            println("Max: ${pingStats["max"]} ms")
            println("Mdev: ${pingStats["mdev"]} ms")
            println("Packet Loss: ${pingStats["packetLoss"]}%")
            return pingStats["packetLoss"]?.toIntOrNull()
        } else {
            println("Impossible de trouver les statistiques de ping dans la sortie.")
            return -1
        }
    }

    @Composable
    fun ICMPDemo() {
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
                        ICMP()
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

    @Composable
    fun ICMP() {
        var serverAddress = remember { mutableStateOf("") }
        var nbPacket = remember { mutableStateOf("") }
        var ping by remember { mutableStateOf("zoumiz") }
        val period = remember { mutableStateOf("") }
        val expectedResult = remember { mutableStateOf("") }
        val expectedResultList = listOf("true", "false")
        val unitTime = remember { mutableStateOf("") }
        val time = listOf("Secondes", "Minutes", "Heures", "Jours")
        val testList = listOf("Server availability", "Response time")
        val packetTime = remember { mutableStateOf("") }
        val testChoosen = remember { mutableStateOf("") }
        var abovePacketTime = remember { mutableStateOf("zoumiz") }
        var maxTime = remember { mutableStateOf<Double?>(0.0) }
        var coroutine = rememberCoroutineScope();
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {
            InputTextField(text = serverAddress, modifier = Modifier.padding(bottom = 8.dp), label = "Server's address")
            InputTextFieldNumber(text = nbPacket, modifier = Modifier.padding(bottom = 8.dp), label = "Nombre packet")
            DropDownMenu(testList, testChoosen, modifier = Modifier.padding(bottom = 8.dp))
            if(testChoosen.value == "Server availability"){
                DropDownMenu(expectedResultList, expectedResult, modifier = Modifier.padding(bottom = 8.dp))
            } else if(testChoosen.value == "Response time"){
                InputTextFieldNumber(text = packetTime, modifier = Modifier.padding(bottom = 8.dp), label = "Max response time")
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
                    if (ping == expectedResult.value) {
                        println(ping)
                        println(expectedResult.value)
                        Text("Le test est passer !", Modifier.align(Alignment.Center))
                    }
                    if(abovePacketTime.value == "false"){
                        println(abovePacketTime)
                        Text("Le test est passer !", Modifier.align(Alignment.Center))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutine.launch(IO) {
                        try {
                            if(testChoosen.value == "Server availability"){
                                var loss = ping(nbPacket, serverAddress)
                                if (loss != null) {
                                    if (loss < 0 || loss > 0){
                                        println("No work")
                                        ping = "false"
                                    } else {
                                        println("work")
                                        ping = "true"
                                    }
                                }
                            } else if(testChoosen.value == "Response time"){
                                maxTime.value = pingMaxTime(nbPacket, serverAddress)
                                println("maxtime"+maxTime)
                                if (maxTime != null) {
                                    if(maxTime.value!! > packetTime.value.toIntOrNull()!!){
                                        println("No work")
                                        abovePacketTime.value = "true"
                                    } else {
                                        println("work")
                                        abovePacketTime.value = "false"
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                , shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }
            Spacer(modifier = Modifier.height(16.dp)) // Add some space before the divider
            Divider(color = Color.Gray, thickness = 2.dp) // Add a horizontal line
            LaunchedEffect(expectedResult.value, serverAddress.value, nbPacket.value, period.value,unitTime.value,packetTime.value,testChoosen.value ) {
                println("Je susi ")
                ping = "zoumiz"
                abovePacketTime.value = "zoumiz"
                maxTime.value = 0.0
            }
        }

    }
}
