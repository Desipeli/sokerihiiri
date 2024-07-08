package com.example.sokerihiiri.ui.navigation

sealed class Screens(val route: String, val title: String) {
    object Main : Screens(route = "main", title="Sokerihiiri")
    object Measurement : Screens(route = "measurement", title="Mittaus")
    object Insulin : Screens(route = "insulin", title="Insuliini")
    object Meal : Screens(route = "meal", title="Ateria")
    object Browse : Screens(route = "browse", title="Selaa") {
        object Main : Screens(route = "browse_main", title="Selaa")
        object Measurements : Screens(route = "browse_measurements", title="Mittaukset") {
            object Main : Screens(route = "browse_measurements_main", title="Mittaukset")
            object Measurement : Screens(route = "browse_measurements_measurement/{id}", title="Mittaus")
        }
        object Injections : Screens(route = "browse_injections", title="Insuliini") {
            object Main : Screens(route = "browse_injections_main", title="Insuliini")
            object Injection : Screens(route = "browse_injections_injection/{id}", title="Insuliini")
        }
        object Meals: Screens(route = "browse_meals", title="Ateriat") {
            object Main : Screens(route = "browse_meals_main", title="Ateriat")
            object Meal : Screens(route = "browse_meals_meal/{id}", title="Ateria")
        }
    }
    object Settings : Screens(route = "settings", title="Asetukset")
}