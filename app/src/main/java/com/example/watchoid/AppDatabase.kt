package com.example.watchoid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.watchoid.dao.AlertDAO
import com.example.watchoid.dao.HTTPTestDAO
import com.example.watchoid.dao.ICMPTestDAO
import com.example.watchoid.dao.LogDAO
import com.example.watchoid.dao.SettingsDAO
import com.example.watchoid.dao.TCPTestDAO
import com.example.watchoid.dao.TapTapDAO
import com.example.watchoid.dao.UDPTestDAO
import com.example.watchoid.dao.UserDao
import com.example.watchoid.entity.Alerts
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.ICMPTest
import com.example.watchoid.entity.Log
import com.example.watchoid.entity.TCPTest
import com.example.watchoid.entity.TapTapGames
import com.example.watchoid.entity.UDPTest
import com.example.watchoid.entity.User

@Database(entities = [User::class, TCPTest::class, ICMPTest::class, UDPTest::class, Alerts::class, HTTPTest::class, com.example.watchoid.entity.Settings::class, TapTapGames::class, Log::class], version = 1)
@TypeConverters(ListConvert::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tcpTest(): TCPTestDAO
    abstract fun icmpTest(): ICMPTestDAO
    abstract fun udpTest(): UDPTestDAO
    abstract fun alerts() : AlertDAO
    abstract fun http_test() : HTTPTestDAO
    abstract fun settingsTable() : SettingsDAO
    abstract fun taptapTable() : TapTapDAO
    abstract  fun log() : LogDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}