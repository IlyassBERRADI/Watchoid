package com.example.watchoid

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class ResponseComponent (
    val value: MutableState<String> = mutableStateOf(""),
    val valueType: MutableState<String> = mutableStateOf(""),
    val charset: MutableState<String> = mutableStateOf("")
)