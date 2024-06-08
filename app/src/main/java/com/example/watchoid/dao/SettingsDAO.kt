package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchoid.entity.Alerts
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.Settings

@Dao
interface SettingsDAO {
    @Insert
    suspend fun insert(settings: Settings)

    @Update
    suspend fun update(settings: Settings)

    @Delete
    suspend fun delete(settings: Settings)

    @Query("SELECT * FROM settings")
    suspend fun getAllSettings(): List<Settings>

    @Query("SELECT * FROM settings WHERE protocol = :protocol")
    suspend fun getSettingByProtocol(protocol: String): Settings?

    @Query("SELECT nb_error FROM settings WHERE protocol = :protocol")
    suspend fun getNbAlertByProtocol(protocol: String): Int
}