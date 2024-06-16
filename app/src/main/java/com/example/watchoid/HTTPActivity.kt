package com.example.watchoid

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchoid.composant.Background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern
import java.io.IOException
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.watchoid.entity.Alerts
import com.example.watchoid.service.TestService
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


class HTTPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HTTPTest()
        }
    }

    fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")


    @Composable
    fun HTTPTest(){
        val coroutineScope = rememberCoroutineScope()
        var responseBody by remember { mutableStateOf("") }
        val state = rememberScrollState()
        val state2 = rememberScrollState()
        var url by rememberSaveable { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("") }
        var pattern by remember { mutableStateOf("") }
        var path by remember { mutableStateOf("") }
        var selectedType2 by remember { mutableStateOf("") }
        var result: Boolean? by remember { mutableStateOf(null) }
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state),
            horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {
            Background(text = "HTTP Test", main = false)
            Spacer(modifier = Modifier.height(30.dp))
            DropdownMenuWithTextField(
                listOf("Text", "Html", "JSON"),
                label = "Entrez votre URL ici",
                onValueChanged = { selectedType = it
                                 Log.i("selectedType", selectedType)}, // Fournir une fonction lambda vide pour onValueChanged
                onTextChange = { url = it })
            Spacer(modifier = Modifier.height(20.dp))

            when (selectedType) {
                "Text" -> {
                    OutlinedTextField(
                        value = pattern,
                        onValueChange = { pattern = it },
                        label = { Text("Pattern") }
                    )
                }
                "Html" -> {
                    OutlinedTextField(
                        value = path,
                        onValueChange = { path = it },
                        label = { Text("Tag") }
                    )
                    OutlinedTextField(
                        value = pattern,
                        onValueChange = { pattern = it },
                        label = { Text("Pattern") }
                    )
                }
                "JSON" -> {
                    OutlinedTextField(
                        value = path,
                        onValueChange = { path = it },
                        label = { Text("Path") }
                    )
                    DropdownMenuWithTextField(
                        listOf("Int", "Long", "Double", "Boolean", "String"),
                        label = "Entrez votre valeur ici",
                        onValueChanged = { selectedType2 = it },
                        onTextChange = { pattern = it }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Réponse :", modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 28.dp))
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp) // Hauteur du rectangle
                    .verticalScroll(state2)
                    .background(color = Color.White) // Couleur du rectangle
            ) {
                    Text(
                        text = responseBody,
                        modifier = Modifier.align(Alignment.Center)
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (result!=null){
                Text(text = result.toString())
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (TCPActivity.isConnectedToNetwork(context)){
                            try {
                                responseBody = HTTPClient.requestGET(url)
                                result = when (selectedType) {
                                    "Text" -> HTTPClient.findInText(pattern, responseBody)
                                    "Html" -> HTTPClient.findInHtml(path, responseBody, pattern)
                                    "JSON" -> HTTPClient.findInJSON(responseBody, path, pattern, selectedType2)
                                    else -> null
                                }
                            } catch (e : IOException){
                                result = false
                            }


                            var test = com.example.watchoid.entity.HTTPTest(url = url, typeRequest = selectedType, pattern = pattern, path = path, typePattern = selectedType2)
                            var idTest = MainActivity.database.http_test().insert(test)

                            var alert = Alerts(idTest = idTest.toInt(), testType = "HTTP", nbError = 0 )
                            MainActivity.database.alerts().insert(alert)
                            withContext(Dispatchers.Main){
                                Toast.makeText(context, "Test enregistré", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(context, "Aucune connexion réseau!!", Toast.LENGTH_SHORT).show()}
                    }
                }
                , shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }

        }

    }


    companion object {
        suspend fun automaticHTTPTest(context: Context) {
            var result : Boolean
            var responseBody : String
            val service = AlertNotificationService(context)
            val query = TCPActivity.selectAllFrom("http_tests")
            var tests = MainActivity.database.http_test().getAllTests(query)
            var period : Long = MainActivity.database.settingsTable().getSettingByProtocol("HTTP")?.periodicity?.toLong()
                ?: 0
            var periodicityUnit = MainActivity.database.settingsTable().getSettingByProtocol("HTTP")?.timeUnitPeriodicity
            when(periodicityUnit){
                "Min" -> period *= 60
                "Heure" -> period *= 60*60
                "Jour" -> period *= 24*60*60
            }
            while (true) {
                for (test in tests) {
                    if (TCPActivity.isConnectedToNetwork(context)) {
                        try {
                            responseBody = HTTPClient.requestGET(test.url)
                            when(test.typeRequest){
                                "Html" -> result = HTTPClient.findInHtml(test.path, responseBody, test.pattern)
                                "Text" -> result = HTTPClient.findInText(test.pattern, responseBody)
                                "JSON" -> result = HTTPClient.findInJSON(responseBody, test.path, test.pattern, test.typePattern)
                                else -> result = false
                            }
                        } catch (e : IOException){
                            result = false
                        }
                        var log = com.example.watchoid.entity.Log(idTest = test.id_test, date = LocalDateTime.now().toString(), testType = "HTTP", result = result)
                        MainActivity.database.log().insert(log)
                        var firstAlert = MainActivity.database.alerts().getAlertByTestId(test.id_test, "HTTP")
                        var alert : Alerts
                        if (firstAlert != null && firstAlert.nbError!=10 && !result) {
                            alert = Alerts(id_alert = firstAlert.id_alert, idTest = test.id_test, testType = "HTTP", nbError = (firstAlert?.nbError
                                ?: 0) +1)
                            MainActivity.database.alerts().update(alert)
                            if (alert.nbError==MainActivity.database.settingsTable().getNbAlertByProtocol("HTTP")) {
                                service.showNotificationAlert(test.id_test, "HTTP")
                            }
                        }
                        else if (firstAlert != null && firstAlert.nbError==10 && result){
                            service.showNotificationAlert2(test.id_test, "HTTP")
                            alert = Alerts(id_alert = firstAlert.id_alert, idTest = test.id_test, testType = "HTTP", nbError = 0)
                            MainActivity.database.alerts().update(alert)
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

    }


}
