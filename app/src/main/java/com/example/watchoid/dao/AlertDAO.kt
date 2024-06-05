package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchoid.entity.Alerts
@Dao
interface AlertDAO {
    @Insert
    suspend fun insert(alert: Alerts)

    @Update
    suspend fun update(alert: Alerts)

    @Delete
    suspend fun delete(alert: Alerts)

    @Query("SELECT * FROM alerts")
    suspend fun getAllTests(): List<Alerts>

    @Query("SELECT * FROM alerts WHERE id_test = :id LIMIT 1")
    suspend fun getTestById(id: Int): Alerts?
}