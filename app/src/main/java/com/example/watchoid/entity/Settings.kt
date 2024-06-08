package com.example.watchoid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) val idSetting: Int = 0,
    @ColumnInfo(name = "protocol") val protocol: String,
    @ColumnInfo(name = "periodicity") val periodicity: Int,
    @ColumnInfo(name = "time_unit_periodicity") val timeUnitPeriodicity: String,
    @ColumnInfo(name = "time_before_deletion") val timeBeforeDeletion: Int,
    @ColumnInfo(name = "time_unit_deletion") val timeUnitDeletion: String,
    @ColumnInfo(name = "nb_error") val nbError: Int
)
