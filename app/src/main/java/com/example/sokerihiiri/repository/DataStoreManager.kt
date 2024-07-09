package com.example.sokerihiiri.repository


import android.content.Context
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
}
