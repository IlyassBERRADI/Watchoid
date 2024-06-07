package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) val id_test: Int = 0,
    @ColumnInfo(name = "test_type") val testType: String,
    @ColumnInfo(name = "nb_error") val nbError: Int
)
