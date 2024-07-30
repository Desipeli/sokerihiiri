package com.example.sokerihiiri.utils

// Funktiot pisteen korvaamiseen pilkulla ja toisin päin

fun floatToCommaString(float: Float): String {
    return float.toString().replace(".", ",")
}

fun commaStringToFloat(commaString: String): Float {
    return commaString.replace(",", ".").toFloat()
}