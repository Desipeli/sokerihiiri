package com.example.sokerihiiri.repository


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
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
    private val LATEST_INSULIN_TIMESTAMP = longPreferencesKey("latest_insulin_TIMESTAMP")
    private val INSULIN_DEADLINE_ENABLED = booleanPreferencesKey("insuline_deadline_enabled")
    private val INSULIN_DEADLINE_Hours = intPreferencesKey("insulin_deadline_hours")
    private val INSULIN_DEADLINE_Minutes = intPreferencesKey("insulin_deadline_minutes")


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

    fun getLatestInsulinTimestamp(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[LATEST_INSULIN_TIMESTAMP]
        }
    }

    suspend fun setLatestInsulinTimestamp(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[LATEST_INSULIN_TIMESTAMP] = timestamp
        }
    }

    fun getInsulinDeadlineHours(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[INSULIN_DEADLINE_Hours] ?: 0
        }
    }

    fun getInsulinDeadlineMinutes(): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[INSULIN_DEADLINE_Minutes] ?: 0
        }
    }

    fun getInsulinDeadlineEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[INSULIN_DEADLINE_ENABLED] ?: false
        }
    }

    suspend fun setInsulinDeadlineEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[INSULIN_DEADLINE_ENABLED] = enabled
        }
    }
    suspend fun setInsulinDeadline(hours: Int, minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[INSULIN_DEADLINE_Hours] = hours
            preferences[INSULIN_DEADLINE_Minutes] = minutes
        }
    }
}
