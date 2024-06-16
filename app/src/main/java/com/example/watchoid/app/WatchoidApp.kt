package com.example.watchoid.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class WatchoidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "testChannelId",
                "Network tests notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val channel2 = NotificationChannel(
                "alertChannelId",
                "Network tests alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager2.createNotificationChannel(channel2)

            val channel3 = NotificationChannel(
                "ConnectionChannelId",
                "Network Connection alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager3 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager3.createNotificationChannel(channel3)
        }
    }
}