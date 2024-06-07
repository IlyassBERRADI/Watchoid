package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "taptap")
data class TapTapGames(
    @PrimaryKey(autoGenerate = true) val id_test: Int = 0,
    @ColumnInfo(name = "record") val record: Int
)
