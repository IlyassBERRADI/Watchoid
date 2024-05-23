package com.example.watchoid

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class RowComponent(
    val period: MutableState<String> = mutableStateOf(""),
    val valueType: MutableState<String> = mutableStateOf(""),
    val charset: MutableState<String> = mutableStateOf(""),
    val isChecked:  MutableState<Boolean> = mutableStateOf(false)
)