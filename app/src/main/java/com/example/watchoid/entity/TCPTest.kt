package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tcp_tests")
data class TCPTest(
    @PrimaryKey(autoGenerate = true) val id_test: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "dst_ip") val dstIp: String,
    @ColumnInfo(name = "test_result") val testResult: String,
    @ColumnInfo(name = "test_attendu") val testAttendu: String,
    @ColumnInfo(name = "nb_period") val nbPerio: Long,
    @ColumnInfo(name = "periodicity") val periodicity: String
)
