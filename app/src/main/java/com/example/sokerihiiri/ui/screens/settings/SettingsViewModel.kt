package com.example.sokerihiiri.ui.screens.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.writeInsulinInjectionsToDownloadsCSV
import com.example.sokerihiiri.utils.writeMealsToDownloadsCSV
import com.example.sokerihiiri.utils.writeMeasurementsToDownloadsCSV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val repository: SokerihiiriRepository
) : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState())
        private set

    fun startWriteToCSV(context: Context, dirUri: Uri, snackbarHostState: SnackbarHostState) {
        context.contentResolver.takePersistableUriPermission(
            dirUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        val documentUri = DocumentsContract.buildDocumentUriUsingTree(
            dirUri,
            DocumentsContract.getTreeDocumentId(dirUri)
        )

        val measurementsFileUri = DocumentsContract.createDocument(
            context.contentResolver, documentUri, "text/csv", "measurements.csv"
        )
        val insulinInjectionsFileUri = DocumentsContract.createDocument(
            context.contentResolver, documentUri, "text/csv", "insulin_injections.csv"
        )
        val mealsFileUri = DocumentsContract.createDocument(
            context.contentResolver, documentUri, "text/csv", "meals.csv"
        )

        writeCSV(context, measurementsFileUri, insulinInjectionsFileUri, mealsFileUri, snackbarHostState)
    }

    private fun writeCSV(
        context: Context,
        measurementsFileUri: Uri?,
        insulinInjectionsFileUri: Uri?,
        mealsFileUri: Uri?,
        snackbarHostState: SnackbarHostState) {

        viewModelScope.launch {
            try {
                    val measurements = repository.getAllBloodSugarMeasurementsAsList()
                    val insulinInjections = repository.getAllInsulinInjectionsAsList()
                    val meals = repository.getAllMealsAsList()

                    val deferredMeasurements = async {
                        measurementsFileUri?.let { fileUri ->
                            writeMeasurementsToDownloadsCSV(
                                context = context,
                                measurements = measurements,
                                fileUri = fileUri
                            )
                        }
                    }
                    val deferredInsulinInjections = async {
                        insulinInjectionsFileUri?.let { fileUri ->
                            writeInsulinInjectionsToDownloadsCSV(
                                context = context,
                                insulinInjections = insulinInjections,
                                fileUri = fileUri
                            )
                        }
                    }
                    val deferredMeals = async {
                        mealsFileUri?.let { fileUri ->
                            writeMealsToDownloadsCSV(
                                context = context,
                                meals = meals,
                                fileUri = fileUri
                            )
                        }
                    }
                    deferredMeasurements.await()
                    deferredInsulinInjections.await()
                    deferredMeals.await()
                    withContext(Dispatchers.Main) {
                        snackbarHostState.showSnackbar("Tiedostot tallennettu")
                    }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error writing CSV", e)
                withContext(Dispatchers.Main) {
                    snackbarHostState.showSnackbar("Virhe")
                }
            }
        }
    }

    init {
        Log.d("SettingsViewModel", "init")
        viewModelScope.launch {
            dataStoreManager.getDefaultInsulinDose().collect { dose ->
                uiState = uiState.copy(defaultInsulinDose = dose)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getDefaultHoursAfterMeal().collect { hours ->
                uiState = uiState.copy(defaultHoursAfterMeal = hours)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getDefaultMinutesAfterMeal().collect { minutes ->
                uiState = uiState.copy(defaultMinutesAfterMeal = minutes)
            }
        }
    }

    fun setDefaultInsulinDose(dose: Int) {
        uiState = uiState.copy(defaultInsulinDose = dose)
    }

    fun setDefaultHoursAfterMeal(hours: Int) {
        uiState = uiState.copy(defaultHoursAfterMeal = hours)
    }

    fun setDefaultMinutesAfterMeal(minutes: Int) {
        uiState = uiState.copy(defaultMinutesAfterMeal = minutes)
    }

    private fun saveDefaultInsulinDose() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultInsulinDose(uiState.defaultInsulinDose)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default insulin dose", e)
            }
        }
    }

    private fun saveDefaultHoursAfterMeal() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultHoursAfterMeal(uiState.defaultHoursAfterMeal)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default hours after meal", e)
            }
        }
    }

    private fun saveDefaultMinutesAfterMeal() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultMinutesAfterMeal(uiState.defaultMinutesAfterMeal)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default minutes after meal", e)
            }
        }
    }

    fun saveDefaultsSettings() {
        saveDefaultInsulinDose()
        saveDefaultHoursAfterMeal()
        saveDefaultMinutesAfterMeal()
    }

    fun deleteAllMeasurements() {
        viewModelScope.launch {
            repository.deleteAllBloodSugarMeasurements()
        }
    }

    fun deleteAllInsulinInjections() {
        viewModelScope.launch {
            repository.deleteAllInsulinInjections()
        }
    }

    fun deleteAllMeals() {
        viewModelScope.launch {
            repository.deleteAllMeals()
        }
    }

    fun deleteAllRoomData() {
        viewModelScope.launch {
            repository.deleteAllMeals()
        }
        viewModelScope.launch {
            repository.deleteAllBloodSugarMeasurements()
        }
        viewModelScope.launch {
            repository.deleteAllInsulinInjections()
        }
    }
}

data class UiState(
    val defaultInsulinDose: Int = 0,
    val defaultHoursAfterMeal: Int = 0,
    val defaultMinutesAfterMeal: Int = 0
)