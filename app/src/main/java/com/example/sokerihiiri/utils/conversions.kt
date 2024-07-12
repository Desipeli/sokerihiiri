package com.example.sokerihiiri.utils

fun floatToCommaString(float: Float): String {
    return float.toString().replace(".", ",")
}