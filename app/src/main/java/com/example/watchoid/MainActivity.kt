package com.example.watchoid

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.watchoid.ui.theme.WatchoidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.InetSocketAddress

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchoidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    val coroutineScope2 = rememberCoroutineScope()
                    var text by remember { mutableStateOf("Response will show here") }
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = text)
                        Button(onClick = {
                            coroutineScope.launch(IO) {
                                var server = InetSocketAddress("www.google.fr", 80)
                                text=TCPClientWeb.getResponse("GET / HTTP/1.1\r\nHost: www.google.fr\r\n\r\n", server)
                            }
                            /*coroutineScope.launch(IO) {
                                var server = InetSocketAddress( 7777)
                                text=TCPClient.getResponse("abc", server)
                            }
                            coroutineScope2.launch(IO){
                                TCPServer(7777).launch()
                            }*/
                        }) {
                            Text("Send")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WatchoidTheme {
        Greeting("Android")
    }
}