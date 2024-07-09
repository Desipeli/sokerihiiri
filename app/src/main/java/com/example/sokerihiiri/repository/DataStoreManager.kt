package com.example.sokerihiiri.repository


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val DEFAULT_INSULIN_DOSE = intPreferencesKey("default_insulin_dose")
    private val DEFAULT_HOURS_AFTER_MEAL = intPreferencesKey("default_hours_after_meal")
    private val DEFAULT_MINUTES_AFTER_MEAL = intPreferencesKey("default_minutes_after_meal")

    fun getDefaultInsulinDose(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[DEFAULT_INSULIN_DOSE] ?: 0
        }
    }

    suspend fun setDefaultInsulinDose(dose: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_INSULIN_DOSE] = dose
        }
    }

    fun getDefaultHoursAfterMeal(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[DEFAULT_HOURS_AFTER_MEAL] ?: 0
        }
    }

    suspend fun setDefaultHoursAfterMeal(hours: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_HOURS_AFTER_MEAL] = hours
        }
    }

    fun getDefaultMinutesAfterMeal(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[DEFAULT_MINUTES_AFTER_MEAL] ?: 0
        }
    }

    suspend fun setDefaultMinutesAfterMeal(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_MINUTES_AFTER_MEAL] = minutes
        }
    }
}
