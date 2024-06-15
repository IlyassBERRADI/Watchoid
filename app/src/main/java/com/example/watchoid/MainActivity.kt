package com.example.watchoid

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.NavigationButton
import com.example.watchoid.entity.Settings
import com.example.watchoid.entity.TCPTest
import com.example.watchoid.entity.User
import com.example.watchoid.service.TestService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var database: AppDatabase
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        database = AppDatabase.getDatabase(this)
        setContent {
            Background(text = "Watchoid", true)
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationButton("New Tests", Protocol_chooser::class)
                NavigationButton("See Logs", LogActivity::class)
                val context = LocalContext.current
                Button(onClick = {
                    val intent : Intent = Intent(context, TestService::class.java)
                    intent.action = TestService.Actions.START.toString()
                    context.startService(intent)
                }, shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                    Text(text = "Start tests", fontSize = 20.sp)
                }
                Button(onClick = {
                    val intent : Intent = Intent(context, TestService::class.java)
                    intent.action = TestService.Actions.STOP.toString()
                    context.startService(intent)
                }, shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor  = Color(0xFF2E698A))) {
                    Text(text = "Stop tests", fontSize = 20.sp)
                }
            }
            applicationContext.deleteDatabase("my_database");
            var coroutine = rememberCoroutineScope();

            coroutine.launch {
                if (database.settingsTable().getAllSettings().isEmpty()){
                    var list = listOf(
                        Settings(protocol = "HTTP", periodicity = 10, timeUnitPeriodicity = "Min", timeBeforeDeletion = 2000, timeUnitDeletion = "Min", nbError = 10),
                        Settings(protocol = "UDP", periodicity = 10, timeUnitPeriodicity = "Min", timeBeforeDeletion = 2000, timeUnitDeletion = "Min", nbError = 10),
                        Settings(protocol = "TCP", periodicity = 10, timeUnitPeriodicity = "Min", timeBeforeDeletion = 2000, timeUnitDeletion = "Min", nbError = 10),
                        Settings(protocol = "ICMP", periodicity = 10, timeUnitPeriodicity = "Min", timeBeforeDeletion = 2000, timeUnitDeletion = "Min", nbError = 10))
                    list.forEach {
                        database.settingsTable().insert(it)
                    }
                }
                if (database.userDao().getAllUsers().isEmpty()){
                    val newUser = User(username = "JohnDoe", email = "john@example.com")
                    database.userDao().insertUser(newUser)
                }

            }

        }

    }

}