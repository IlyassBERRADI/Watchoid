package com.example.watchoid.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.watchoid.R
import com.example.watchoid.TCPActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TestService : Service() {
    private lateinit var coroutineScope: CoroutineScope

    /*companion object {
        private const val testChannelId = "testChannelId"
        private const val channelName = "Network tests notifications"
        private const val minTimeLocationUpdateInMillisecond = 10000L
        private const val minDistanceLocationUpdateInMeter = 1000F
    }*/

    /*override fun onCreate() {
        notificationService()
        super.onCreate()
    }


    private fun notificationService() {
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo (API level 26) and higher, use notification channels
            val channel = NotificationChannel(
                testChannelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Running service to execute automatic tests"
            }
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(channel)
            }
            Notification.Builder(this, testChannelId)
        } else {
            // For API levels below 26, create notifications without channels
            Notification.Builder(this)
        }

        // Build the notification
        notificationBuilder.apply {
            // Set the title of the notification
            setContentTitle("Test service")
            // Set the notification to ongoing (i.e., not dismissible)
            setOngoing(true)
            // Set the content text of the notification
            setContentText("Running service to execute automatic tests")
            // Set the small icon for the notification
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }

        // Start the service in the foreground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo (API level 26) and higher, use startForeground with notification channel
            startForeground(1, notificationBuilder.build())
        } else {
            // For API levels below 26, use startForeground without notification channel
            startForeground(1, notificationBuilder.build())
        }
    }*/


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun start(){
        coroutineScope = CoroutineScope(IO)
        val notification = Notification.Builder(this, "testChannelId")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Test service")
            .setContentText("Running service to execute automatic tests")
            .build()
        startForeground(1, notification)
        coroutineScope.launch {
            TCPActivity.automaticTCPTest(this@TestService)
        }
    }

    private fun stop() {
        coroutineScope.cancel()  // Cancel the scope to stop the coroutine
        stopSelf()  // Stop the service
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    enum class Actions {
        START, STOP
    }

}