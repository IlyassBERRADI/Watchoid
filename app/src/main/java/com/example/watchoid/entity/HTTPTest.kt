package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "http_tests")
data class HTTPTest(
    @PrimaryKey(autoGenerate = true) val id_test: Int = 0,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "type_request") val typeRequest: String,
    @ColumnInfo(name = "pattern") val pattern: String,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "type_pattern") val typePattern: String,
)
