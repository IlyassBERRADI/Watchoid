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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableIntStateOf


class HTTPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HTTPTest()
        }
    }


    @Composable
    fun HTTPTest(){
        val coroutineScope = rememberCoroutineScope()
        var responseBody by remember { mutableStateOf("") }
        val state = rememberScrollState()
        val state2 = rememberScrollState()
        var url by rememberSaveable { mutableStateOf("") }
        var selectedType by remember { mutableStateOf("") }
        var pattern by remember { mutableStateOf("") }
        var tag by remember { mutableStateOf("") }
        var position by remember { mutableIntStateOf(-1) }
        var path by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }
        var selectedType2 by remember { mutableStateOf("") }
        var result: Boolean? by remember { mutableStateOf(null) }
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
            //Log.i("selectedtype", selectedType)
            /*if (selectedType=="Text"){
                OutlinedTextField(
                    value = pattern,
                    onValueChange = { pattern = it },
                    label = { Text("Pattern") }
                )
            }
            else if (selectedType=="Html"){
                OutlinedTextField(
                    value = tag,
                    onValueChange = { tag = it },
                    label = { Text("Tag") }
                )
                OutlinedTextField(
                    value = position.toString(),
                    onValueChange = { position = it.toInt() },
                    label = { Text("Position") }
                )
            }
            else if (selectedType=="JSON"){
                OutlinedTextField(
                    value = path,
                    onValueChange = { path = it },
                    label = { Text("Path") }
                )
                DropdownMenuWithTextField(
                    listOf("Int", "Long", "Double", "Boolean", "String"),
                    label = "Entrez votre valeur ici",
                    onValueChanged = { selectedType2 = it }, // Fournir une fonction lambda vide pour onValueChanged
                    onTextChange = { value = it })
            }*/
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
                        value = tag,
                        onValueChange = { tag = it },
                        label = { Text("Tag") }
                    )
                    OutlinedTextField(
                        value = if (position == -1) "" else position.toString(),
                        onValueChange = { position = it.toIntOrNull() ?: -1 },
                        label = { Text("Position") }
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
                        onTextChange = { value = it }
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
                    /*Thread {
                        val request = Request.Builder().url(url).build()
                        val client = OkHttpClient()
                        client.newCall(request).enqueue(object : Callback {
                            override fun onResponse(call: Call, response: Response) {
                                responseBody = response.body?.string().toString()
                                // responseBody contient la réponse JSON
                            }

                            override fun onFailure(call: Call, e: IOException) {
                                // Gérer les erreurs de connexion ici
                                e.printStackTrace()
                            }
                        })

                    }.start()*/
                    coroutineScope.launch(Dispatchers.IO) {
                        responseBody = HTTPClient.requestGET(url)
                        Log.i("pattern", pattern)
                        result = when (selectedType) {
                            "Text" -> HTTPClient.findInText(pattern, responseBody)
                            "Html" -> HTTPClient.findInHtml(tag, position, responseBody)
                            "JSON" -> HTTPClient.findInJSON(responseBody, path, value, selectedType2)
                            else -> null
                        }
                        /*if (selectedType=="Text"){
                            result = HTTPClient.findInText(pattern, responseBody)
                        }
                        else if (selectedType=="Html"){
                            result = HTTPClient.findInHtml(tag, position, responseBody)
                        }
                        else if (selectedType=="JSON"){
                            result = HTTPClient.findInJSON(responseBody, path, value, selectedType2)
                        }*/
                    }
                }
                , shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }

        }

    }


    /*@Composable
    fun HTTPTest() {
        var url = URL("https://www.google.com/")
        var response by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Hauteur du rectangle
                .background(color = Color.White) // Couleur du rectangle
        ) {
            if (response) {
                Text(
                    text = "La balise à été trouvé",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if(error){
                Text(
                    text = "La balise n'à pas été trouvé",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {

                    Thread {
                        val connection = url.openConnection() as HttpURLConnection
                        connection.requestMethod = "GET"

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val inputReader = BufferedReader(InputStreamReader(connection.inputStream))
                            val responses = StringBuilder()

                            var inputLine: String?
                            while (inputReader.readLine().also { inputLine = it } != null) {
                                responses.append(inputLine)
                            }
                            inputReader.close()
                            val regex = "<h12>(.*?)</h12>"
                            val pattern: Pattern = Pattern.compile(regex)
                            val matcher = pattern.matcher(responses.toString())
                            if (matcher.find()) {
                                val titre = matcher.group(1)
                                println("Texte entre les balises <title> et </title> : $titre")
                                response = true;
                                error = false
                            } else {
                                println("Balise <title> non trouvée dans le texte.")
                                error = true
                            }
                        } else {
                            error = true
                        }
                    }.start()
                }
                , shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }
        }
    }*/
}
