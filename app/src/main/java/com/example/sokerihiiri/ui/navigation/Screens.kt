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
            object Measurement : Screens(route = "browse_measurements_measurement", title="Mittaus")
        }
        object Injections : Screens(route = "browse_injections", title="Insuliini") {
            object Main : Screens(route = "browse_injections_main", title="Insuliini")
            object Injection : Screens(route = "browse_injections_injection", title="Insuliini")
        }
        object Meals: Screens(route = "browse_meals", title="Ateriat") {
            object Main : Screens(route = "browse_meals_main", title="Ateriat")
            object Meal : Screens(route = "browse_meals_meal", title="Ateria")
        }
    }
    object Settings : Screens(route = "settings", title="Asetukset") {
        object Main : Screens(route = "settings_main", title="Asetukset")
        object Defaults : Screens(route = "settings_defaults", title="Oletusarvot")
        object ControlData : Screens(route = "settings_control_data", title="Tiedot")
        object Notifications: Screens(route = "settings_notifications", title="Ilmoitukset")

    }
}

val screenList = listOf(
    Screens.Main,
    Screens.Measurement,
    Screens.Insulin,
    Screens.Meal,
    Screens.Browse.Main,
    Screens.Browse.Measurements.Main,
    Screens.Browse.Injections.Main,
    Screens.Browse.Meals.Main,
    Screens.Settings.Main,
    Screens.Settings.Defaults,
    Screens.Settings.ControlData,
    Screens.Settings.Notifications
)
