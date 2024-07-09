package com.example.sokerihiiri.repository


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object DataStoreManager {
    private val DEFAULT_INSULIN_DOSE = intPreferencesKey("default_insulin_dose")

    fun getDefaultInsulinDose(context: Context): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[DEFAULT_INSULIN_DOSE] ?: 0
        }
    }

    suspend fun setDefaultInsulinDose(context: Context, dose: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_INSULIN_DOSE] = dose
        }
    }
}
