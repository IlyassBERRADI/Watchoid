package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alerts"/*,
    foreignKeys = [
        ForeignKey(
            entity = UDPTest::class,
            parentColumns = ["id_test"],
            childColumns = ["id_test"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TCPTest::class,
            parentColumns = ["id_test"],
            childColumns = ["id_test"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ICMPTest::class,
            parentColumns = ["id_test"],
            childColumns = ["id_test"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HTTPTest::class,
            parentColumns = ["id_test"],
            childColumns = ["id_test"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_test"])]*/
)
data class Alerts(
    @PrimaryKey(autoGenerate = true) val id_alert: Int = 0,
    @ColumnInfo(name = "id_test") val idTest: Int,
    @ColumnInfo(name = "test_type") val testType: String,
    @ColumnInfo(name = "nb_error") val nbError: Int
)
