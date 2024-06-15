package com.example.watchoid

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
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
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.DropDownMenu
import com.example.watchoid.composant.InputTextField
import com.example.watchoid.composant.InputTextFieldNumber
import com.example.watchoid.entity.Alerts
import com.example.watchoid.entity.ICMPTest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime


class ICMPActivityUser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "ICMP Test", main = false)
            ICMPDemo()
        }
    }
    fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")
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

    fun pingMaxTime(nbPacket: MutableState<String>, serverAddress: MutableState<String>): Map<String, String> {
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
            return pingStats
        } else {
            println("Impossible de trouver les statistiques de ping dans la sortie.")
            return emptyMap()
        }
    }

    fun ping(nbPacket: MutableState<String>, serverAddress: MutableState<String>):Map<String, String> {
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
            return pingStats
        } else {
            println("Impossible de trouver les statistiques de ping dans la sortie.")
            return emptyMap()
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

    @SuppressLint("CoroutineCreationDuringComposition")
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
        var avgTime = remember { mutableStateOf<Double?>(0.0) }
        var minTime = remember { mutableStateOf<Double?>(0.0) }
        var coroutine = rememberCoroutineScope();
        var coroutineAlert = rememberCoroutineScope();
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
                    coroutineAlert.launch(IO) {
                        val query = selectAllFrom("icmp_tests")
                        var id = MainActivity.database.icmpTest()
                        var list = id.getAllTests(query)
                        MainActivity.database.alerts().incrementNbError(list.size, "ICMP")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutine.launch(IO) {
                        try {
                            if(testChoosen.value == "Server availability"){
                                var allTimeArray = ping(nbPacket, serverAddress)
                                var packetloss = allTimeArray["packetLoss"]?.toIntOrNull()
                                maxTime.value = allTimeArray["max"]?.toDoubleOrNull()
                                avgTime.value = allTimeArray["avg"]?.toDoubleOrNull()
                                minTime.value = allTimeArray["min"]?.toDoubleOrNull()
                                if (packetloss != null) {
                                    if (packetloss < 0 || packetloss > 0){
                                        println("No work")
                                        ping = "false"
                                    } else {
                                        println("work")
                                        ping = "true"
                                    }
                                }
                            } else if(testChoosen.value == "Response time"){
                                var timeTab = pingMaxTime(nbPacket, serverAddress)
                                maxTime.value = timeTab["max"]?.toDoubleOrNull()
                                avgTime.value = timeTab["avg"]?.toDoubleOrNull()
                                minTime.value = timeTab["min"]?.toDoubleOrNull()
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

                        if(testChoosen.value == "Server availability"){
                            var test = ICMPTest(nbPacket = nbPacket.value,date = "10/10/2020", dstIp = serverAddress.value, nbPerio = period.value.toLong(), periodicity =  unitTime.value, testAttendu = expectedResult.value, testResult = ping, testType =  testChoosen.value, tpsAvg = avgTime.value, tpsMax = maxTime.value, tpsMin = minTime.value)
                            MainActivity.database.icmpTest().insert(test)
                            val query = selectAllFrom("icmp_tests")
                            var id = MainActivity.database.icmpTest()

                            var list = id.getAllTests(query)
                            var alert = Alerts(idTest = list.size, testType = "ICMP", nbError = 0)
                            MainActivity.database.alerts().insert(alert)

                        } else if(testChoosen.value == "Response time"){
                            var test = ICMPTest(nbPacket = nbPacket.value,date = "10/10/2020", dstIp = serverAddress.value, nbPerio = period.value.toLong(), periodicity =  unitTime.value, testAttendu = packetTime.value, testResult =  maxTime.value.toString(), testType = testChoosen.value, tpsAvg = avgTime.value, tpsMax = maxTime.value, tpsMin = minTime.value)
                            MainActivity.database.icmpTest().insert(test)
                            val query = selectAllFrom("icmp_tests")
                            var id = MainActivity.database.icmpTest()

                            var list = id.getAllTests(query)
                            var alert = Alerts(idTest = list.size, testType = "ICMP", nbError = 0)
                            MainActivity.database.alerts().insert(alert)
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
                ping = "zoumiz"
                abovePacketTime.value = "zoumiz"
                maxTime.value = 0.0
            }
        }

    }

    companion object{

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

        fun pingMaxTime(nbPacket: String, serverAddress: String): Map<String, String> {
            val command = "ping -c ${nbPacket} ${serverAddress}"
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
                return pingStats
            } else {
                println("Impossible de trouver les statistiques de ping dans la sortie.")
                return emptyMap()
            }
        }

        fun ping(nbPacket: String, serverAddress: String):Map<String, String> {
            val command = "ping -c ${nbPacket} ${serverAddress}"
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
                return pingStats
            } else {
                println("Impossible de trouver les statistiques de ping dans la sortie.")
                return emptyMap()
            }
        }
        fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")
        suspend fun automaticICMPTest(ctx : Context){
            println("Je suis là bg")
            var isStopped : Boolean
            var result : Boolean
            var resultText : String
            var connectedToNetwork : Boolean
            var context = ctx//LocalContext.current
            var maxTime = mutableStateOf<Double?>(0.0)
            var avgTime = mutableStateOf<Double?>(0.0)
            var minTime = mutableStateOf<Double?>(0.0)
            var response : String = ""
            var ping = mutableStateOf("zoumiz")
            var abovePacketTime = mutableStateOf("zoumiz")
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
            val query = selectAllFrom("icmp_tests")
            var tests = MainActivity.database.icmpTest().getAllTests(query)
            var period : Long = MainActivity.database.settingsTable().getSettingByProtocol("ICMP")?.periodicity?.toLong()?:0
            while(true){
                for(test in tests){
                    println("connectedToNetwork"+connectedToNetwork)
                    if(connectedToNetwork){
                        if(test.testType == "Server availability"){
                            println("availability")
                            var allTimeArray = ping(test.nbPacket, test.dstIp)
                            var packetloss = allTimeArray["packetLoss"]?.toIntOrNull()
                            maxTime.value = allTimeArray["max"]?.toDoubleOrNull()
                            avgTime.value = allTimeArray["avg"]?.toDoubleOrNull()
                            minTime.value = allTimeArray["min"]?.toDoubleOrNull()
                            if (packetloss != null) {
                                if (packetloss < 0 || packetloss > 0){
                                    ping.value = "false"
                                } else {
                                    ping.value = "true"
                                }
                            }
                        }
                    } else if(test.testType == "Response time"){
                        println("Response time")
                        var timeTab = pingMaxTime(test.nbPacket, test.dstIp)
                        maxTime.value = timeTab["max"]?.toDoubleOrNull()
                        avgTime.value = timeTab["avg"]?.toDoubleOrNull()
                        minTime.value = timeTab["min"]?.toDoubleOrNull()
                        if (maxTime != null) {
                            if(maxTime.value!! > test.testAttendu.toIntOrNull()!!){
                                println("No work")
                                abovePacketTime.value = "true"
                            } else {
                                println("work")
                                abovePacketTime.value = "false"
                            }
                        }
                    }
                    if(test.testType == "Server availability"){
                        var testInDB= ICMPTest(nbPacket = test.nbPacket,date = "10/10/2020", dstIp = test.dstIp, nbPerio = test.nbPerio, periodicity =  test.periodicity, testAttendu = test.testAttendu, testResult = ping.value, testType =  test.testType, tpsAvg = avgTime.value, tpsMax = maxTime.value, tpsMin = minTime.value)
                        MainActivity.database.icmpTest().insert(testInDB)
                        var id = MainActivity.database.icmpTest()

                        var list = id.getAllTests(query)
                        var alert = Alerts(idTest = list.size, testType = "ICMP", nbError = 0)
                        MainActivity.database.alerts().insert(alert)
                        var log = com.example.watchoid.entity.Log(idTest = test.id_test, date = LocalDateTime.now().toString(), testType = "ICMP", result = ping.value.toBoolean())
                        MainActivity.database.log().insert(log)

                    } else if(test.testType == "Response time"){
                        var testInDB = ICMPTest(nbPacket = test.nbPacket,date = "10/10/2020", dstIp = test.dstIp, nbPerio = test.nbPerio, periodicity =  test.periodicity, testAttendu = test.testAttendu, testResult =  maxTime.value.toString(), testType = test.testType, tpsAvg = avgTime.value, tpsMax = maxTime.value, tpsMin = minTime.value)
                        MainActivity.database.icmpTest().insert(testInDB)
                        var id = MainActivity.database.icmpTest()

                        var list = id.getAllTests(query)
                        var alert = Alerts(idTest = list.size, testType = "ICMP", nbError = 0)
                        MainActivity.database.alerts().insert(alert)
                        var log = com.example.watchoid.entity.Log(idTest = test.id_test, date = LocalDateTime.now().toString(), testType = "ICMP", result = abovePacketTime.value.toBoolean())
                        MainActivity.database.log().insert(log)
                    }
                }
                delay(period*1000)
            }
        }
    }

}
