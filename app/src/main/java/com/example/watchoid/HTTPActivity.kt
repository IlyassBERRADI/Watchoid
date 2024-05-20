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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import okhttp3.*
import java.io.IOException


class HTTPActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Background(text = "HTTP Test", main = false)
            JsonTest()
            //HTTPTest()
        }
    }


    @Composable
    fun JsonTest(){
        val coroutineScope = rememberCoroutineScope()
        var responseBody by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Alignement horizontal au centre
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Hauteur du rectangle
                    .background(color = Color.White) // Couleur du rectangle
            ) {
                    Text(
                        text = responseBody,
                        modifier = Modifier.align(Alignment.Center)
                    )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {val url = "https://dog.ceo/api/breeds/image/random"
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
                        responseBody = HTTPClient.getRequest()
                    }
                }
                , shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                Text("Envoyer")
            }

        }

    }


    @Composable
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
    }
}
