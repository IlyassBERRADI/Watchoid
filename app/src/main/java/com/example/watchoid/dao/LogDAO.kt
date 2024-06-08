package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchoid.entity.ICMPTest
import com.example.watchoid.entity.Log


@Dao
interface LogDAO {

    @Insert
    suspend fun insert(log: Log)

    @Update
    suspend fun update(log: Log)

    @Delete
    suspend fun delete(log: Log)

    @RawQuery
    suspend fun getAllLogs(query: SupportSQLiteQuery): List<Log>

    @Query("SELECT * FROM log WHERE id_test = :id LIMIT 1")
    suspend fun getLogById(id: Int): Log?
}