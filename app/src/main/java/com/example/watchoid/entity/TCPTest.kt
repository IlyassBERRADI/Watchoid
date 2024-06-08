package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "tcp_tests")
data class TCPTest(
    @PrimaryKey(autoGenerate = true) val idTest: Int = 0,
    @ColumnInfo(name = "request") val request: String,
    @ColumnInfo(name = "type_request") val typeRequest: String,
    @ColumnInfo(name = "size_before_string") val sizeBeforeString: Boolean,
    @ColumnInfo(name = "close_input") val closeInput: Boolean,
    @ColumnInfo(name = "in_encoding") val inEncoding: String,
    @ColumnInfo(name = "type_response") val typeResponse: String,
    @ColumnInfo(name = "out_encoding") val outEncoding: String,
    @ColumnInfo(name = "size_response") val sizeResponse: Int?,
    @ColumnInfo(name = "resultat_attendu") val resultExpected: String,
    @ColumnInfo(name = "dst_ip") val dstIp: String,
    @ColumnInfo(name = "dst_port") val dstPort: Int
)
