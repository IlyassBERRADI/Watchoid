package com.example.watchoid.dao

import androidx.room.Dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchoid.entity.ICMPTest
import com.example.watchoid.entity.UDPTest

@Dao
interface ICMPTestDAO {
    @Insert
    suspend fun insert(icmpTest: ICMPTest)

    @Update
    suspend fun update(icmpTest: ICMPTest)

    @Delete
    suspend fun delete(icmpTest: ICMPTest)

    @RawQuery
    suspend fun getAllTests(query: SupportSQLiteQuery): List<ICMPTest>

    @Query("SELECT * FROM icmp_tests WHERE id_test = :id LIMIT 1")
    suspend fun getTestById(id: Int): ICMPTest?
}