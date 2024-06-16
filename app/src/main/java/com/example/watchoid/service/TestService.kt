package com.example.watchoid.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.watchoid.HTTPActivity
import com.example.watchoid.ICMPActivityUser
import com.example.watchoid.R
import com.example.watchoid.TCPActivity
import com.example.watchoid.UDPActivityUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TestService : Service() {
    private lateinit var coroutineScope: CoroutineScope



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
        coroutineScope.launch {
            HTTPActivity.automaticHTTPTest(this@TestService)
        }
        coroutineScope.launch {
            ICMPActivityUser.automaticICMPTest(this@TestService)
        }
        coroutineScope.launch {
            UDPActivityUser.automaticUDPTest(this@TestService)
        }
    }

    private fun stop() {
        if (::coroutineScope.isInitialized){
            coroutineScope.cancel()
            stopSelf()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    enum class Actions {
        START, STOP
    }

}