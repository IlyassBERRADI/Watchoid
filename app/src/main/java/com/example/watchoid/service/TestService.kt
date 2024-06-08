package com.example.watchoid.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.compose.ui.platform.LocalContext
import com.example.watchoid.R
import com.example.watchoid.TCPActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TestService : Service() {
    companion object {
        private const val testChannelId = "testChannelId"
        private const val channelName = "testName"
        private const val minTimeLocationUpdateInMillisecond = 10000L
        private const val minDistanceLocationUpdateInMeter = 1000F
    }

    override fun onCreate() {
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
            //setSmallIcon(R.drawable.ic_notifcation_icon)
        }

        // Start the service in the foreground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo (API level 26) and higher, use startForeground with notification channel
            startForeground(1, notificationBuilder.build())
        } else {
            // For API levels below 26, use startForeground without notification channel
            startForeground(1, notificationBuilder.build())
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(IO).launch {
            TCPActivity.automaticTCPTest(this@TestService)
        }
        //notificationService()
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}