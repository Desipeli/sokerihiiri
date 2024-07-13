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
import com.example.sokerihiiri.utils.FileType
import com.example.sokerihiiri.utils.MAX_INSULIN_DOSE
import com.example.sokerihiiri.utils.determineFileContent
import com.example.sokerihiiri.utils.readInsulinInjectionsFromCSV
import com.example.sokerihiiri.utils.readMealsFromCSV
import com.example.sokerihiiri.utils.readMeasurementsFromCSV
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

    fun setLoadingFileName(fileName: String) {
        uiState = uiState.copy(loadingFileName = fileName)
    }

    fun setLoadingFileUri(uri: Uri) {
        uiState = uiState.copy(loadingFileUri = uri)
    }
    fun checkFileContent(context: Context, uri: Uri, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            val fileType = determineFileContent(context, uri)
            when (fileType) {
                FileType.MEASUREMENTS -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = "Haluatko varmasti korvata sovelluksessa olevat mittaustulokset tiedoston ${uiState.loadingFileName} sisällöllä?")
                    uiState = uiState.copy(loadingFileType = FileType.MEASUREMENTS)
                }
                FileType.INSULIN -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = "Haluatko varmasti korvata sovelluksessa olevat insuliinitiedot tiedoston ${uiState.loadingFileName} sisällöllä?")
                    uiState = uiState.copy(loadingFileType = FileType.INSULIN)
                }
                FileType.MEALS -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = "Haluatko varmasti korvata sovelluksessa olevat ateriatiedot tiedoston ${uiState.loadingFileName} sisällöllä?")
                    uiState = uiState.copy(loadingFileType = FileType.MEALS)
                }
                else -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = "")
                    uiState = uiState.copy(loadingFileType = null)
                    snackbarHostState.showSnackbar("Virhe. Tarkista, että tiedosto sisältää pakolliset rivit.")
                }
            }
        }

    }

    fun readFileContent(context: Context, snackbarHostState: SnackbarHostState) {
        val uri = uiState.loadingFileUri
        if (uri == null) {
            Log.e("SettingsViewModel", "No file selected")
            return
        }
        viewModelScope.launch {
            try {
                when (uiState.loadingFileType) {
                    FileType.MEASUREMENTS -> {
                        val measurements = readMeasurementsFromCSV(context, uri)
                        val deleteJob = async(Dispatchers.IO) {
                            repository.deleteAllBloodSugarMeasurements()
                        }
                        deleteJob.await()
                        repository.insertManyBloodSugarMeasurements(measurements)
                    }
                    FileType.INSULIN -> {
                        val insulinInjections = readInsulinInjectionsFromCSV(context, uri)
                        val deleteJob = async(Dispatchers.IO) {
                            repository.deleteAllInsulinInjections()
                        }
                        deleteJob.await()
                        repository.insertManyInsulinInjections(insulinInjections)
                    }
                    FileType.MEALS -> {
                        val meals = readMealsFromCSV(context, uri)
                        val deleteJob = async(Dispatchers.IO) {
                            repository.deleteAllMeals()
                        }
                        deleteJob.await()
                        repository.insertManyMeals(meals)
                    }
                    else -> {
                        snackbarHostState.showSnackbar("Virhe")
                    }
                }
                snackbarHostState.showSnackbar("Valmis")
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Virhe")
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
        if (dose <= MAX_INSULIN_DOSE) {
            uiState = uiState.copy(defaultInsulinDose = dose)
        }
    }

    fun setDefaultHoursAfterMeal(hours: Int) {
        uiState = uiState.copy(defaultHoursAfterMeal = hours)
    }

    fun setDefaultMinutesAfterMeal(minutes: Int) {
        uiState = uiState.copy(defaultMinutesAfterMeal = minutes)
    }

    private suspend fun saveDefaultInsulinDose() {
        try {
            dataStoreManager.setDefaultInsulinDose(uiState.defaultInsulinDose)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun saveDefaultHoursAfterMeal() {
        try {
            dataStoreManager.setDefaultHoursAfterMeal(uiState.defaultHoursAfterMeal)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun saveDefaultMinutesAfterMeal() {
        try {
            dataStoreManager.setDefaultMinutesAfterMeal(uiState.defaultMinutesAfterMeal)
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Error saving default minutes after meal", e)
            throw e
        }
    }

    fun saveDefaultsSettings(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                saveDefaultInsulinDose()
                saveDefaultHoursAfterMeal()
                saveDefaultMinutesAfterMeal()
                snackbarHostState.showSnackbar("Asetukset tallennettu")
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Virhe")
            }
        }
    }

    fun deleteAllMeasurements(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                repository.deleteAllBloodSugarMeasurements()
                snackbarHostState.showSnackbar("Mittaustiedot poistettu")
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all measurements", e)
                snackbarHostState.showSnackbar("Virhe")
            }
        }
    }

    fun deleteAllInsulinInjections(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                repository.deleteAllInsulinInjections()
                snackbarHostState.showSnackbar("Insuliinitiedot poistettu")
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all insulin injections", e)
                snackbarHostState.showSnackbar("Virhe")
            }
        }
    }

    fun deleteAllMeals(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                repository.deleteAllMeals()
                snackbarHostState.showSnackbar("Ateriatiedot poistettu")
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all meals", e)
                snackbarHostState.showSnackbar("Virhe")
            }
        }
    }

    fun deleteAllRoomData(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            deleteAllMeals(snackbarHostState)
        }
        viewModelScope.launch {
            deleteAllMeasurements(snackbarHostState)
        }
        viewModelScope.launch {
            deleteAllInsulinInjections(snackbarHostState)
        }
    }
}

data class UiState(
    val defaultInsulinDose: Int = 0,
    val defaultHoursAfterMeal: Int = 0,
    val defaultMinutesAfterMeal: Int = 0,
    val loadingFileUri: Uri? = null,
    val loadingFileName: String? = "",
    val loadingFileType: FileType? = null,
    val loadingFileReplaceWarning: String = ""
)