package com.example.sokerihiiri.utils

fun baseRoute(route: String?): String? {
    // Leikataan viimeisin / ja sen jälkeinen osa pois. Näin päästään eroon reitin /{id} osasta.
    return route?.substringBeforeLast("/")
}