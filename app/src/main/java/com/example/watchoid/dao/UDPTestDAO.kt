package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchoid.entity.UDPTest

@Dao
interface UDPTestDAO {
    @Insert
    suspend fun insert(udpTest: UDPTest)

    @Update
    suspend fun update(udpTest: UDPTest)

    @Delete
    suspend fun delete(udpTest: UDPTest)

    @RawQuery
    suspend fun getAllTests(query: SupportSQLiteQuery): List<UDPTest>

    @Query("SELECT * FROM udp_tests WHERE id_test = :id LIMIT 1")
    suspend fun getTestById(id: Int): UDPTest?
}