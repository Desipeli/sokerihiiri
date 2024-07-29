package com.example.sokerihiiri.ui.screens.injections.insulin

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.R
import com.example.sokerihiiri.SokerihiiriApplication
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.MAX_INSULIN_DOSE
import com.example.sokerihiiri.utils.dateAndTimeToUTCLong
import com.example.sokerihiiri.utils.timestampToHoursAndMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

class InvalidDoseException(message: String) : Exception(message)

@HiltViewModel
class InsulinViewModel @Inject constructor(
    private val repository: SokerihiiriRepository,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    var uiState: InsulinUiState by mutableStateOf(InsulinUiState())
        private set

    init {
        resetState()
    }
    fun setDose(dose: Int) {
        if (dose <= MAX_INSULIN_DOSE) {
            uiState = uiState.copy(dose = dose, doseError = null)
        }
    }

    fun setDate(date: Long) {
        uiState = uiState.copy(date = date)
    }

    fun setTime(hour: Int, minute: Int) {
        uiState = uiState.copy(hour = hour, minute = minute)
    }

    fun setComment(comment: String) {
        uiState = uiState.copy(comment = comment)
    }

    fun resetState() {
        uiState = InsulinUiState()
        viewModelScope.launch {
            dataStoreManager.getDefaultInsulinDose().collect {
                setDose(it)
            }
        }
    }

    fun saveInsulinInjection(context: Context) {
        try {
            validateFields(context = context)
            Log.d("saveInsulinInjection", "Saving insulin injection")
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val insulinInjection = InsulinInjection(
                dose = uiState.dose,
                timestamp = dateTime,
                comment = uiState.comment
            )

            viewModelScope.launch {
                repository.insertInsulinInjection(insulinInjection)
            }
            resetState()
        } catch (e: InvalidDoseException) {
            uiState = uiState.copy(doseError = e.message)
            throw e
        }
        catch (e: Exception) {
            Log.e("InsulinViewModel", "Error saving insulin injection", e)
        }
    }

    fun getInsulinInjectionById(id: String?) {
        if (id.toString() == uiState.id.toString()) return
        if (id == null) {
            resetState()
            return
        }

        viewModelScope.launch {
            val insulinInjection = repository.getInsulinInjectionById(id.toInt())
            val (hour, minute) = timestampToHoursAndMinutes(insulinInjection.timestamp)
            uiState = InsulinUiState(
                id = insulinInjection.id,
                dose = insulinInjection.dose,
                date = insulinInjection.timestamp,
                hour = hour,
                minute = minute,
                comment = insulinInjection.comment
            )
        }
    }

    fun updateInsulinInjection(context: Context) {
        try {
            validateFields(context)
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val insulinInjection = InsulinInjection(
                id = uiState.id!!,
                dose = uiState.dose,
                timestamp = dateTime,
                comment = uiState.comment
            )
            viewModelScope.launch {
                repository.updateInsulinInjection(insulinInjection)
            }
        } catch (e: InvalidDoseException) {
            uiState = uiState.copy(doseError = e.message)
            Log.e("InsulinViewModel", "Invalid dose", e)
            throw e
        } catch (e: Exception) {
            Log.e("InsulinViewModel", "Error updating insulin injection", e)
        }
    }

    fun deleteInsulinInjectionById() {
        try {
            viewModelScope.launch {
                repository.deleteInsulinInjectionById(uiState.id!!)
            }
        } catch (e: Exception) {
            Log.e("InsulinViewModel", "Error deleting insulin injection", e)
        }
    }

    private fun validateFields(context: Context) {
        Log.d("validateFields", "Validating fields")
        Log.d("validateFields", "dose: ${uiState.dose}")
        if (uiState.dose <= 0) {
            throw InvalidDoseException(context.getString(R.string.insulin_dose_exception))
        }
    }

    fun setCanEdit(canEdit: Boolean) {
        uiState = uiState.copy(canEdit = canEdit)
    }
}

data class InsulinUiState(
    val id: Int? = null,
    val dose: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val hour: Int = LocalTime.now().hour,
    val minute: Int = LocalTime.now().minute,
    val comment: String = "",
    val doseError: String? = null,
    val canEdit: Boolean = false
    )