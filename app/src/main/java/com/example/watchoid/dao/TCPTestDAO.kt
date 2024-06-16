package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.TCPTest
@Dao
interface TCPTestDAO {
    @Insert
    suspend fun insert(tcpTest: TCPTest): Long

    @Update
    suspend fun update(tcpTest: TCPTest)

    @Delete
    suspend fun delete(tcpTest: TCPTest)

    @RawQuery
    suspend fun getAllTests(query: SupportSQLiteQuery): List<TCPTest>

    @Query("SELECT * FROM tcp_tests WHERE idTest = :id")
    suspend fun getTestById(id: Int): TCPTest?
}