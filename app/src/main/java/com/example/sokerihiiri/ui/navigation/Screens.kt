package com.example.sokerihiiri.ui.navigation

import com.example.sokerihiiri.R

// https://www.freepik.com/icon/mouse_375105
// https://www.freepik.com/icon/vaccine_3214025
// https://www.freepik.com/icon/sugar-blood-level_11748885
// https://www.freepik.com/icon/meal_3274099
// https://www.freepik.com/icon/mouse_8277661



sealed class Screens(val route: String, val title: String, val logo: Int? = null) {
    object Main : Screens(route = "main", title="Sokerihiiri", logo = R.drawable.mouse)
    object Measurements : Screens(route = "measurements", title="Mittaukset") {
        object Main : Screens(route = "measurements_main", title="Mittaukset", logo = R.drawable.blood_sugar)
        object EditMeasurement : Screens(route = "measurements_edit_measurement", title="Mittaus", logo = R.drawable.blood_sugar)
        object NewMeasurement : Screens(route = "measurements_new_measurement", title="Mittaus", logo = R.drawable.blood_sugar)
    }
    object Injections : Screens(route = "injections", title="Insuliini") {
        object Main : Screens(route = "injections_main", title="Insuliini", logo = R.drawable.insulin_color)
        object EditInjection : Screens(route = "injections_edit_injection", title="Insuliini", logo = R.drawable.insulin_color)
        object NewInjection : Screens(route = "injections_new_injection", title="Insuliini", logo = R.drawable.insulin_color)
    }
    object Meals: Screens(route = "meals", title="Ateriat") {
        object Main : Screens(route = "meals_main", title="Ateriat", logo = R.drawable.meal)
        object EditMeal : Screens(route = "meals_edit_meal", title="Ateria", logo = R.drawable.meal)
        object NewMeal : Screens(route = "meals_new_meal", title="Ateria", logo = R.drawable.meal)
    }
    //}
    object Settings : Screens(route = "settings", title="Asetukset") {
        object Main : Screens(route = "settings_main", title="Asetukset", logo = R.drawable.mouse_settings)
        object Defaults : Screens(route = "settings_defaults", title="Oletusarvot", logo = R.drawable.mouse_settings)
        object ControlData : Screens(route = "settings_control_data", title="Tiedot", logo = R.drawable.mouse_settings)
        object Notifications: Screens(route = "settings_notifications", title="Ilmoitukset", logo = R.drawable.mouse_settings)

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
