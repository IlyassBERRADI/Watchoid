package com.example.watchoid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.watchoid.composant.Background
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.ICMPTest
import com.example.watchoid.entity.TCPTest
import com.example.watchoid.entity.UDPTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class Log : ComponentActivity() {
    fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var anyList: List<Any>
        setContent {
            Background(text = "Logs", main = false)
            selectOption()

        }

    }

    @Composable
    fun selectOption(){
        var anyList = remember { mutableStateOf<List<Any>>(emptyList()) }
        var selectedOption = remember { mutableStateOf("") }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
            horizontalArrangement = Arrangement.Center){
            Button(onClick = {
                CoroutineScope(IO).launch {
                    var udp = MainActivity.database.udpTest()
                    selectedOption.value = "UDP"
                    val tableName = "udp_tests"
                    val query = selectAllFrom(tableName)
                    anyList.value = udp.getAllTests(query)
                }
            }, shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text(text = "UDP")
            }
            Button(onClick = {
                CoroutineScope(IO).launch {
                    var udp = MainActivity.database.udpTest()
                    selectedOption.value = "TCP"
                    val tableName = "tcp_tests"
                    val query = selectAllFrom(tableName)
                    anyList.value = udp.getAllTests(query)
                }
            }, shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text(text = "TCP")
            }
            Button(onClick = {
                CoroutineScope(IO).launch {
                    var udp = MainActivity.database.icmpTest()
                    selectedOption.value = "ICMP"
                    val tableName = "icmp_tests"
                    val query = selectAllFrom(tableName)
                    anyList.value = udp.getAllTests(query)
                }
            }, shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text(text = "ICMP")
            }
            Button(onClick = {
                CoroutineScope(IO).launch {
                    var udp = MainActivity.database.udpTest()
                    selectedOption.value = "HTTP"
                    val tableName = "http_tests"
                    val query = selectAllFrom(tableName)
                    anyList.value = udp.getAllTests(query)
                }
            }, shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text(text = "HTTP")
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 150.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(selectedOption.value){
                "UDP" ->{
                    val testList: List<UDPTest> = anyList.value.mapNotNull { it as? UDPTest }
                    testList.forEach {
                        Row {
                            Text(text = "${it.id_test}"+" ${it.dstIp}"+" ${it.testResult}"+" ${it.testAttendu}"+" ${it.date}")
                        }
                    }
                }
                "TCP" ->{
                    val testList: List<TCPTest> = anyList.value.mapNotNull { it as? TCPTest }
                    testList.forEach {
                        Row {
                            Text(text = "Test ID : ${it.id_test} "+"IP Destination ${it.dstIp} "+"Test Result ${it.testResult} "+"Test Expected Result :  ${it.testAttendu} "+"Date : ${it.date}")
                        }
                    }
                }
                "HTTP" ->{
                    val testList: List<HTTPTest> = anyList.value.mapNotNull { it as? HTTPTest }
                    testList.forEach {
                        Row {
                            Box(modifier = Modifier.fillMaxWidth().padding(10.dp)){
                                Text(text = "${it.id_test}"+" ${it.dstIp}"+" ${it.testType}"+" ${it.testResult}"+" ${it.testAttendu}"+" ${it.date}")
                            }
                        }
                    }
                }
                "ICMP" ->{
                    val testList: List<ICMPTest> = anyList.value.mapNotNull { it as? ICMPTest }
                    testList.forEach {
                        Row {
                            Box(modifier = Modifier.fillMaxWidth().padding(10.dp)){
                                Text(text = "Test ID ${it.id_test} "+"IP Destination ${it.dstIp} "+"Test Type : ${it.testType} "+"Test Result ${it.testResult}"+"Test Expected Result : ${it.testAttendu} "+"Minimal Time : ${it.tpsMin}"+"Average Time : ${it.tpsAvg}"+"Maximum Time :  ${it.tpsMax}"+"Date : ${it.date}")
                            }
                        }
                    }
                }
            }

        }
    }

}


