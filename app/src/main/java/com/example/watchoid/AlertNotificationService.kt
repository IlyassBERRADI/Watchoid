package com.example.watchoid

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class AlertNotificationService(
    private val context : Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    fun showNotificationAlert(idTest : Int, protocol : String){
        val nottification = NotificationCompat.Builder(context, "alertChannelId")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Alerte tests réseaux")
            .setContentText("Le test $protocol avec l'id $idTest a echoué plusieurs fois")
            .build()
        notificationManager.notify(2, nottification)

    }

    fun showNotificationAlert2(idTest : Int, protocol : String){
        val nottification = NotificationCompat.Builder(context, "alertChannelId")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Alerte tests réseaux")
            .setContentText("Le test $protocol avec l'id $idTest marche a nouveau")
            .build()
        notificationManager.notify(3, nottification)

    }

    fun showNotificationInternet(){
        val nottification = NotificationCompat.Builder(context, "ConnectionChannelId")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Alerte connexion")
            .setContentText("La connexion internet est perdue, les tests sont tous arrêtés")
            .build()
        notificationManager.notify(2, nottification)

    }


}