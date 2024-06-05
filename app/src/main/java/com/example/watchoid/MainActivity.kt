package com.example.watchoid

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.watchoid.composant.Background
import com.example.watchoid.composant.NavigationButton
import com.example.watchoid.entity.TCPTest
import com.example.watchoid.entity.User
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var database: AppDatabase
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            Background(text = "Watchoid", true)
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavigationButton("New Tests", Protocol_chooser::class)
                NavigationButton("See Logs", Log::class)
            }
            applicationContext.deleteDatabase("my_database");
            var coroutine = rememberCoroutineScope();
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "my_database"
            ).build()
            coroutine.launch {
                val tcp = TCPTest(date = "20/10/10",sourceIp = "10.0.0.2", dstIp = "10.0.2.2", tpsRep = 30L, testAttendu = "Bonjour", testResult = "Bonjour", error = null, nbAlert = 2L, nbPerio = 5, periodicity = "Minutes" )
                database.tcpTest().insert(tcp)
                val newUser = User(username = "JohnDoe", email = "john@example.com")
                database.userDao().insertUser(newUser)
            }

        }

    }

}