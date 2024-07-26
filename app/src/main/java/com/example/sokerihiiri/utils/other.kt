package com.example.sokerihiiri.utils

fun baseRoute(route: String?): String? {
    return route?.substringBeforeLast("/")
}