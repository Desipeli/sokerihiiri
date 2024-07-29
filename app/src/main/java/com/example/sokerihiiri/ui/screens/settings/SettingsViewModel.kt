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
import androidx.work.WorkManager
import com.example.sokerihiiri.R
import com.example.sokerihiiri.notifications.cancelInsulinNotification
import com.example.sokerihiiri.notifications.scheduleInsulinNotification
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.FileType
import com.example.sokerihiiri.utils.MAX_INSULIN_DOSE
import com.example.sokerihiiri.utils.determineFileContent
import com.example.sokerihiiri.utils.readInsulinInjectionsFromCSV
import com.example.sokerihiiri.utils.readMealsFromCSV
import com.example.sokerihiiri.utils.readMeasurementsFromCSV
import com.example.sokerihiiri.utils.readOthersFromCSV
import com.example.sokerihiiri.utils.writeInsulinInjectionsToDownloadsCSV
import com.example.sokerihiiri.utils.writeMealsToDownloadsCSV
import com.example.sokerihiiri.utils.writeMeasurementsToDownloadsCSV
import com.example.sokerihiiri.utils.writeOthersToDownloadsCSV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val repository: SokerihiiriRepository,
    private val workManager: WorkManager
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
        val othersFileUri = DocumentsContract.createDocument(
            context.contentResolver, documentUri, "text/csv", "others.csv"
        )

        writeCSV(context, measurementsFileUri, insulinInjectionsFileUri, mealsFileUri, othersFileUri, snackbarHostState)
    }

    private fun writeCSV(
        context: Context,
        measurementsFileUri: Uri?,
        insulinInjectionsFileUri: Uri?,
        mealsFileUri: Uri?,
        othersFileUri: Uri?,
        snackbarHostState: SnackbarHostState) {

        viewModelScope.launch {
            try {
                val measurements = repository.getAllBloodSugarMeasurementsAsList()
                val insulinInjections = repository.getAllInsulinInjectionsAsList()
                val meals = repository.getAllMealsAsList()
                val others = repository.getAllOthersAsList()

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
                val deferredOthers = async {
                    othersFileUri?.let { fileUri ->
                        writeOthersToDownloadsCSV(
                            context = context,
                            others = others,
                            fileUri = fileUri
                        )
                    }
                }
                deferredMeasurements.await()
                deferredInsulinInjections.await()
                deferredMeals.await()
                deferredOthers.await()
                withContext(Dispatchers.Main) {
                    snackbarHostState.showSnackbar(context.getString(R.string.data_exported))
                }
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error writing CSV", e)
                withContext(Dispatchers.Main) {
                    snackbarHostState.showSnackbar(context.getString(R.string.error))
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
                    uiState = uiState.copy(loadingFileReplaceWarning = context.getString(
                        R.string.replace_measurements_warning,
                        uiState.loadingFileName
                    ))
                    uiState = uiState.copy(loadingFileType = FileType.MEASUREMENTS)
                }
                FileType.INSULIN -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = context.getString(
                        R.string.replace_insulin_warning,
                        uiState.loadingFileName
                    ))
                    uiState = uiState.copy(loadingFileType = FileType.INSULIN)
                }
                FileType.MEALS -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = context.getString(
                        R.string.replace_meals_warning,
                        uiState.loadingFileName
                    ))
                    uiState = uiState.copy(loadingFileType = FileType.MEALS)
                }
                FileType.OTHERS -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = context.getString(
                        R.string.replace_others_warning,
                        uiState.loadingFileName
                    ))
                    uiState = uiState.copy(loadingFileType = FileType.OTHERS)
                }
                else -> {
                    uiState = uiState.copy(loadingFileReplaceWarning = "")
                    uiState = uiState.copy(loadingFileType = null)
                    snackbarHostState.showSnackbar(context.getString(R.string.import_error))
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
                    FileType.OTHERS -> {
                        val others = readOthersFromCSV(context, uri)
                        val deleteJob = async(Dispatchers.IO) {
                            repository.deleteAllOthers()
                        }
                        deleteJob.await()
                        repository.insertManyOthers(others)
                    }
                    else -> {
                        snackbarHostState.showSnackbar(context.getString(R.string.error))
                    }
                }
                snackbarHostState.showSnackbar(context.getString(R.string.ready))
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(context.getString(R.string.error))
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

        getInsulinDeadline()
        getInsulinNotification()
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

    private fun saveDefaultInsulinDose() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultInsulinDose(uiState.defaultInsulinDose)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun saveDefaultHoursAfterMeal() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultHoursAfterMeal(uiState.defaultHoursAfterMeal)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun saveDefaultMinutesAfterMeal() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultMinutesAfterMeal(uiState.defaultMinutesAfterMeal)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default minutes after meal", e)
                throw e
            }
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

    fun deleteAllMeasurements(snackbarHostState: SnackbarHostState, context: Context) {
        viewModelScope.launch {
            try {
                repository.deleteAllBloodSugarMeasurements()
                snackbarHostState.showSnackbar(context.getString(R.string.measurements_deleted))
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all measurements", e)
                snackbarHostState.showSnackbar(context.getString(R.string.error))
            }
        }
    }

    fun deleteAllInsulinInjections(snackbarHostState: SnackbarHostState, context: Context) {
        viewModelScope.launch {
            try {
                repository.deleteAllInsulinInjections()
                snackbarHostState.showSnackbar(context.getString(R.string.insulin_deleted))
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all insulin injections", e)
                snackbarHostState.showSnackbar(context.getString(R.string.error))
            }
        }
    }

    fun deleteAllMeals(snackbarHostState: SnackbarHostState, context: Context) {
        viewModelScope.launch {
            try {
                repository.deleteAllMeals()
                snackbarHostState.showSnackbar(context.getString(R.string.meals_deleted))
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all meals", e)
                snackbarHostState.showSnackbar(context.getString(R.string.error))
            }
        }
    }

    fun deleteAllOthers(snackbarHostState: SnackbarHostState, context: Context) {
        viewModelScope.launch {
            try {
                repository.deleteAllOthers()
                snackbarHostState.showSnackbar(context.getString(R.string.others_deleted))
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error deleting all others", e)
                snackbarHostState.showSnackbar(context.getString(R.string.error))
            }
        }
    }

    fun deleteAllRoomData(snackbarHostState: SnackbarHostState, context: Context) {
        viewModelScope.launch {
            deleteAllMeals(snackbarHostState, context)
        }
        viewModelScope.launch {
            deleteAllMeasurements(snackbarHostState, context)
        }
        viewModelScope.launch {
            deleteAllInsulinInjections(snackbarHostState, context)
        }
        viewModelScope.launch {
            deleteAllOthers(snackbarHostState, context)
        }
    }

    fun setInsulinNotification(active: Boolean) {
        uiState = uiState.copy(insulinNotification = active)
    }

    fun setInsulinDeadline(hours: Int, minutes: Int) {
        uiState = uiState.copy(insulinDeadlineHours = hours, insulinDeadlineMinutes = minutes)
    }

    private fun getInsulinNotification() {
        viewModelScope.launch {
            dataStoreManager.getInsulinDeadlineEnabled().collect { enabled ->
                uiState = uiState.copy(insulinNotification = enabled)
            }
        }
    }

    fun getInsulinDeadline() {
        viewModelScope.launch {
            dataStoreManager.getInsulinDeadlineHours().collect { hours ->
                uiState = uiState.copy(insulinDeadlineHours = hours)
            }
        }
        viewModelScope.launch {
            dataStoreManager.getInsulinDeadlineMinutes().collect { minutes ->
                uiState = uiState.copy(insulinDeadlineMinutes = minutes)
            }
        }
    }

    fun saveInsulinDeadline(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            try {
                if (uiState.insulinNotification) {
                    dataStoreManager.setInsulinDeadline(
                        uiState.insulinDeadlineHours,
                        uiState.insulinDeadlineMinutes)
                    scheduleInsulinNotification(
                        workManager = workManager,
                        hours = uiState.insulinDeadlineHours,
                        minutes = uiState.insulinDeadlineMinutes)
                    snackbarHostState.showSnackbar("Ilmoitus käytössä")
                } else {
                    cancelInsulinNotification(workManager)
                    snackbarHostState.showSnackbar("Ilmoitus poistettu")
                }
                dataStoreManager.setInsulinDeadlineEnabled(uiState.insulinNotification)

            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving insulin deadline", e)
                snackbarHostState.showSnackbar("Virhe")
            }
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
    val loadingFileReplaceWarning: String = "",
    val insulinNotification: Boolean = false,
    val insulinDeadlineHours: Int = 0,
    val insulinDeadlineMinutes: Int = 0,
)