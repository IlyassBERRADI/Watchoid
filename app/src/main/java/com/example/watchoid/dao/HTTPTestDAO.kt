package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.watchoid.entity.HTTPTest
import com.example.watchoid.entity.ICMPTest

@Dao
interface HTTPTestDAO {
    @Insert
    suspend fun insert(httpTest: HTTPTest): Long

    @Update
    suspend fun update(httpTest: HTTPTest)

    @Delete
    suspend fun delete(httpTest: HTTPTest)

    @RawQuery
    suspend fun getAllTests(query: SupportSQLiteQuery): List<HTTPTest>

    @Query("SELECT * FROM http_tests WHERE id_test = :id LIMIT 1")
    suspend fun getTestById(id: Int): HTTPTest?
}