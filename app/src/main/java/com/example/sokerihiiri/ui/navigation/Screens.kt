package com.example.sokerihiiri.ui.navigation

sealed class Screens(val route: String, val title: String) {
    object Main : Screens(route = "main", title="Sokerihiiri")
    object Measurements : Screens(route = "measurements", title="Mittaukset") {
        object Main : Screens(route = "measurements_main", title="Mittaukset")
        object EditMeasurement : Screens(route = "measurements_edit_measurement", title="Mittaus")
        object NewMeasurement : Screens(route = "measurements_new_measurement", title="Mittaus")
    }
    object Injections : Screens(route = "injections", title="Insuliini") {
        object Main : Screens(route = "injections_main", title="Insuliini")
        object EditInjection : Screens(route = "injections_edit_injection", title="Insuliini")
        object NewInjection : Screens(route = "injections_new_injection", title="Insuliini")
    }
    object Meals: Screens(route = "meals", title="Ateriat") {
        object Main : Screens(route = "meals_main", title="Ateriat")
        object EditMeal : Screens(route = "meals_edit_meal", title="Ateria")
        object NewMeal : Screens(route = "meals_new_meal", title="Ateria")
    }
    //}
    object Settings : Screens(route = "settings", title="Asetukset") {
        object Main : Screens(route = "settings_main", title="Asetukset")
        object Defaults : Screens(route = "settings_defaults", title="Oletusarvot")
        object ControlData : Screens(route = "settings_control_data", title="Tiedot")
        object Notifications: Screens(route = "settings_notifications", title="Ilmoitukset")

    }
}

val screenList = listOf(
    Screens.Main,
    Screens.Measurements,
    Screens.Injections,
    Screens.Meals,
    Screens.Measurements.Main,
    Screens.Measurements.EditMeasurement,
    Screens.Measurements.NewMeasurement,
    Screens.Injections.Main,
    Screens.Injections.EditInjection,
    Screens.Injections.NewInjection,
    Screens.Meals.Main,
    Screens.Meals.EditMeal,
    Screens.Meals.NewMeal,
    Screens.Settings.Main,
    Screens.Settings.Defaults,
    Screens.Settings.ControlData,
    Screens.Settings.Notifications
)
