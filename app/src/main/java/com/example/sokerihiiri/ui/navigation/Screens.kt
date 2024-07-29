package com.example.sokerihiiri.ui.navigation

import com.example.sokerihiiri.R

// https://www.freepik.com/icon/mouse_375105
// https://www.freepik.com/icon/vaccine_3214025
// https://www.freepik.com/icon/sugar-blood-level_11748885
// https://www.freepik.com/icon/meal_3274099
// https://www.freepik.com/icon/mouse_8277661

// Kommentti https://www.freepik.com/icon/message_5356196



sealed class Screens(val route: String, val titleResourceId: Int, val logo: Int? = null) {
    object Main : Screens(route = "main", titleResourceId= R.string.app_name, logo = R.drawable.mouse)
    object Measurements : Screens(route = "measurements", titleResourceId = R.string.measurements_title) {
        object Main : Screens(route = "measurements_main", titleResourceId = R.string.measurements_title, logo = R.drawable.blood_sugar)
        object EditMeasurement : Screens(route = "measurements_edit_measurement", titleResourceId = R.string.measurement_title, logo = R.drawable.blood_sugar)
        object NewMeasurement : Screens(route = "measurements_new_measurement", titleResourceId = R.string.measurement_title, logo = R.drawable.blood_sugar)
    }
    object Injections : Screens(route = "injections", titleResourceId = R.string.insulin_title) {
        object Main : Screens(route = "injections_main", titleResourceId = R.string.insulin_title, logo = R.drawable.insulin_color)
        object EditInjection : Screens(route = "injections_edit_injection", titleResourceId = R.string.insulin_title, logo = R.drawable.insulin_color)
        object NewInjection : Screens(route = "injections_new_injection", titleResourceId = R.string.insulin_title, logo = R.drawable.insulin_color)
    }
    object Meals: Screens(route = "meals", titleResourceId= R.string.meals_title) {
        object Main : Screens(route = "meals_main", titleResourceId= R.string.meals_title, logo = R.drawable.meal)
        object EditMeal : Screens(route = "meals_edit_meal", titleResourceId =R.string.meal_title, logo = R.drawable.meal)
        object NewMeal : Screens(route = "meals_new_meal", titleResourceId =R.string.meal_title, logo = R.drawable.meal)
    }
    object Others: Screens(route = "others", titleResourceId = R.string.others_title) {
        object Main : Screens(route = "others_main", titleResourceId = R.string.others_title, logo = R.drawable.mouse)
        object EditOther : Screens(route = "others_edit_other", titleResourceId = R.string.others_title, logo = R.drawable.mouse)
        object NewOther : Screens(route = "others_new_other", titleResourceId = R.string.others_title, logo = R.drawable.mouse)
    }
    //}
    object Settings : Screens(route = "settings", titleResourceId = R.string.settings_title) {
        object Main : Screens(route = "settings_main", titleResourceId = R.string.settings_title, logo = R.drawable.mouse_settings)
        object Defaults : Screens(route = "settings_defaults", titleResourceId = R.string.defaults_title, logo = R.drawable.mouse_settings)
        object ControlData : Screens(route = "settings_control_data", titleResourceId = R.string.data_settings_title, logo = R.drawable.mouse_settings)
        object Notifications: Screens(route = "settings_notifications", titleResourceId = R.string.notifications_title, logo = R.drawable.mouse_settings)

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
    Screens.Others.Main,
    Screens.Others.EditOther,
    Screens.Others.NewOther,
    Screens.Settings.Main,
    Screens.Settings.Defaults,
    Screens.Settings.ControlData,
    Screens.Settings.Notifications
)
