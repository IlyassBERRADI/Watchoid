package com.example.watchoid.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.example.watchoid.ResponseComponent
import java.nio.ByteBuffer
import java.util.Date

@Entity(tableName = "udp_tests")
data class UDPTest(
    @PrimaryKey(autoGenerate = true) val id_test: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "dst_ip") val dstIp: String,
    @ColumnInfo(name = "dst_port") val dstPort: String,
    @ColumnInfo(name = "buffer") val buffer: ByteBuffer,
    @ColumnInfo(name = "expectedResultList") val expectedResultList: List<String>,
    @ColumnInfo(name = "test_result") val testResult: String,
    @ColumnInfo(name = "test_attendu") val testAttendu: String,
    @ColumnInfo(name = "nb_period") val nbPerio: Long,
    @ColumnInfo(name = "periodicity") val periodicity: String
)
