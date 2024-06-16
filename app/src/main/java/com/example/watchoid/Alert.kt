package com.example.watchoid

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.watchoid.composant.Background
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.ICMPTest
import com.example.watchoid.entity.TCPTest
import com.example.watchoid.entity.UDPTest
import com.example.watchoid.ui.theme.WatchoidTheme
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class Alert : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val icmpList = mutableListOf<ICMPTest>()
            val udpList = mutableListOf<UDPTest>()
            val tcpList = mutableListOf<TCPTest>()
            val httpList = mutableListOf<HTTPTest>()
            Background(text = "Alerts", main = false)
            DisplayTests()
        }
    }
}
fun sendNotification(context: Context, title: String, message: String) {
    val channelId = "test_alerts_channel"
    val notificationId = System.currentTimeMillis().toInt()

    // Create a notification channel for Android Oreo and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        System.out.println("amogus")
        val name = "Test Alerts"
        val descriptionText = "Channel for test alerts"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.notification) // Replace with your app's notification icon
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        System.out.println("amogus")
        notify(notificationId, builder.build())
    }
}
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DisplayTests() {
    val coroutineScope = rememberCoroutineScope()
    var nbICMPAlert by remember { mutableStateOf(0) }
    var nbUDPAlert by remember { mutableStateOf(0) }
    var nbHTTPAlert by remember { mutableStateOf(0) }
    var nbTCPAlert by remember { mutableStateOf(0) }
    var tcpTest by remember { mutableStateOf<TCPTest?>(null) }
    var icmpTest by remember { mutableStateOf<ICMPTest?>(null) }
    val context = LocalContext.current
    var udpTests = remember { mutableStateListOf<UDPTest>() }
    val icmpTests = remember { mutableStateListOf<ICMPTest>() }
    val httpTests = remember { mutableStateListOf<HTTPTest>() }
    var tcpTests = remember { mutableStateListOf<TCPTest>() }

    LaunchedEffect(Unit) {
        val settingsDAO = MainActivity.database.settingsTable()
        nbUDPAlert = settingsDAO.getNbAlertByProtocol("UDP")
        nbICMPAlert = settingsDAO.getNbAlertByProtocol("ICMP")
        nbTCPAlert = settingsDAO.getNbAlertByProtocol("TCP")
        nbHTTPAlert = settingsDAO.getNbAlertByProtocol("HTTP")

        val alertDao = MainActivity.database.alerts()
        val list = alertDao.getAllTests()

        list.forEach {
            when (it.testType) {
                "ICMP" -> {
                    if (it.nbError >= nbICMPAlert) {
                        sendNotification(context, "ICMP Alert", "ICMP test has ${it.nbError} errors")
                        val test = MainActivity.database.icmpTest().getTestById(it.idTest)
                        test?.let { icmpTests.add(it) }
                    }
                }
                "TCP" -> {
                    if (it.nbError >= nbTCPAlert) {
                        sendNotification(context, "TCP Alert", "TCP test has ${it.nbError} errors")
                        val test = MainActivity.database.tcpTest().getTestById(it.idTest)
                        test?.let { tcpTests.add(it) }
                    }
                }
                "UDP" -> {
                    if (it.nbError >= nbUDPAlert) {
                        sendNotification(context, "UDP Alert", "UDP test has ${it.nbError} errors")
                        val test = MainActivity.database.udpTest().getTestById(it.idTest)
                        test?.let { udpTests.add(it) }
                    }
                }
                "HTTP" -> {
                    if (it.nbError >= nbHTTPAlert) {
                        sendNotification(context, "HTTP Alert", "HTTP test has ${it.nbError} errors")
                        val test = MainActivity.database.http_test().getTestById(it.idTest)
                        test?.let { httpTests.add(it) }
                    }
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(top = 80.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("ICMP Tests")
        icmpTests.forEach { test ->
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Test ID : ${test.id_test}, Result : ${test.testResult}")
                }
            }
            Text(text = annotatedString)
        }

        Text("UDP Tests")
        udpTests.forEach { test ->
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Test ID : ${test.id_test}, Result : ${test.testResult}")
                }
            }
            Text(text = annotatedString)
        }

        Text("HTTP Tests")
        httpTests.forEach { test ->
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Test ID : ${test.id_test}, Url : ${test.url}")
                }
            }
            Text(text = annotatedString)
        }

        Text("TCP Tests")
        tcpTests.forEach { test ->
            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Test ID : ${test.idTest}, Request : ${test.request}")
                }
            }
            Text(text = annotatedString)
        }
    }


}
