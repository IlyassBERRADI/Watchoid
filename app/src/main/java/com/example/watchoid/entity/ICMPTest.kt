package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "icmp_tests")
data class ICMPTest(
    @PrimaryKey(autoGenerate = true) val id_test: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    //@ColumnInfo(name = "source_ip") val sourceIp: String,
    @ColumnInfo(name = "dst_ip") val dstIp: String,
    @ColumnInfo(name = "test_type") val testType: String,
    @ColumnInfo(name = "tps_max") val tpsMax: Double?,
    @ColumnInfo(name = "tps_min") val tpsMin: Double?,
    @ColumnInfo(name = "tps_avg") val tpsAvg: Double?,
    @ColumnInfo(name = "test_result") val testResult: String,
    @ColumnInfo(name = "test_attendu") val testAttendu: String,
    @ColumnInfo(name = "nb_perio") val nbPerio: Long,
    @ColumnInfo(name = "periodicity") val periodicity: String,
    @ColumnInfo(name = "nb_alert") val nbAlert: Long
)
