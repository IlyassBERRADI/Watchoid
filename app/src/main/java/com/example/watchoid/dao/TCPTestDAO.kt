package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchoid.entity.TCPTest
@Dao
interface TCPTestDAO {
    @Insert
    suspend fun insert(tcpTest: TCPTest)

    @Update
    suspend fun update(tcpTest: TCPTest)

    @Delete
    suspend fun delete(tcpTest: TCPTest)

    @Query("SELECT * FROM tcp_tests")
    suspend fun getAllTests(): List<TCPTest>

    @Query("SELECT * FROM tcp_tests WHERE id_test = :id LIMIT 1")
    suspend fun getTestById(id: Int): TCPTest?
}