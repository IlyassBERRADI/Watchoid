package com.example.watchoid.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.watchoid.entity.Settings
import com.example.watchoid.entity.TapTapGames

@Dao
interface TapTapDAO {
    @Insert
    suspend fun insert(tapGames: TapTapGames)

    @Update
    suspend fun update(tapGames: TapTapGames)

    @Delete
    suspend fun delete(tapGames: TapTapGames)

    @Query("SELECT * FROM taptap")
    suspend fun getAllTests(): List<TapTapGames>
}