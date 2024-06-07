package com.example.watchoid

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.watchoid.dao.AlertDAO
import com.example.watchoid.dao.HTTPTestDAO
import com.example.watchoid.dao.ICMPTestDAO
import com.example.watchoid.dao.SettingsDAO
import com.example.watchoid.dao.TCPTestDAO
import com.example.watchoid.dao.TapTapDAO
import com.example.watchoid.dao.UDPTestDAO
import com.example.watchoid.dao.UserDao
import com.example.watchoid.entity.Alerts
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.ICMPTest
import com.example.watchoid.entity.TCPTest
import com.example.watchoid.entity.TapTapGames
import com.example.watchoid.entity.UDPTest
import com.example.watchoid.entity.User

@Database(entities = [User::class, TCPTest::class, ICMPTest::class, UDPTest::class, Alerts::class, HTTPTest::class, com.example.watchoid.entity.Settings::class, TapTapGames::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun tcpTest(): TCPTestDAO
    abstract fun icmpTest(): ICMPTestDAO
    abstract fun udpTest(): UDPTestDAO
    abstract fun alerts() : AlertDAO
    abstract fun http_test() : HTTPTestDAO
    abstract fun settingsTable() : SettingsDAO
    abstract fun taptapTable() : TapTapDAO

}