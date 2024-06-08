package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "log")
data class Log(
    @PrimaryKey(autoGenerate = true)
    val idLog : Int = 0,
    @ColumnInfo(name = "id_test")
    val idTest : Int,
    @ColumnInfo(name = "date")
    val date : String,
    @ColumnInfo(name = "test_type")
    val testType : String,
    @ColumnInfo(name = "result")
    val result : Boolean = false
)
