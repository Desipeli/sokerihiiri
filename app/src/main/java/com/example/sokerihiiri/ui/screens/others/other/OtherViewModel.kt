package com.example.sokerihiiri.ui.screens.others.other

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.R
import com.example.sokerihiiri.repository.Other
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.dateAndTimeToUTCLong
import com.example.sokerihiiri.utils.timestampToHoursAndMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
    private val repository: SokerihiiriRepository,
): ViewModel() {
    // ViewModel uuden Muut-tapahtuman luontiin, poistoon ja muokkaukseen

    var uiState: UiState by mutableStateOf(UiState())
        private set

    fun resetState() {
        uiState = UiState()
    }

    init {
        resetState()
    }

    private fun validateFields(): Boolean {
        return uiState.comment.isNotEmpty()
    }

    fun saveOther(context: Context) {
        // Tallennetaan tapahtuma tietokantaan
        try {
            if (!validateFields()) throw Exception(context.getString(R.string.comment_missing))
            val timestamp = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val other = Other(
                timestamp = timestamp,
                comment = uiState.comment,
            )

            viewModelScope.launch {
                repository.insertOther(other)
            }
            resetState()
        } catch (e: Exception) {
            Log.e("OtherViewModel", "saveOther: $e")
            throw e
        }
    }

    fun getOtherById(id: String?) {
        // Haetaan tapahtuma tietokannasta id:n perusteella, paitsi jos tapahtuma on jo haettu tai
        // id == null
        if (id.toString() == uiState.id.toString()) return
        if (id == null) {
            resetState()
            return
        }

        viewModelScope.launch {
            val other = repository.getOtherById(id.toInt())
            val (hour, minute) = timestampToHoursAndMinutes(other.timestamp)
            uiState = UiState(
                id = other.id,
                comment = other.comment,
                date = other.timestamp,
                hour = hour,
                minute = minute,
            )
        }
    }

    fun updateOther(context: Context) {
        // Päivitetään tapahtuma tietokantaan
        try {
            if (!validateFields()) throw Exception(context.getString(R.string.comment_missing))

            val timestamp = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val other = Other(
                id = uiState.id!!,
                timestamp = timestamp,
                comment = uiState.comment,
            )

            viewModelScope.launch {
                repository.updateOther(other)
            }
        } catch (e: Exception) {
            Log.e("OtherViewModel", "updateOther: $e")
            throw e
        }
    }

    fun deleteOther() {
        // Poistetaan tapahtuma tietokannasta
        try {
            viewModelScope.launch {
                repository.deleteOtherById(uiState.id!!)
            }
        } catch (e: Exception) {
            Log.e("OtherViewModel", "deleteOther: $e")
        }
    }

    fun setCanEdit(canEdit: Boolean) {
        uiState = uiState.copy(
            canEdit = canEdit
        )
    }

    fun setComment(comment: String) {
        uiState = uiState.copy(
            comment = comment
        )
    }

    fun setDate(date: Long) {
        uiState = uiState.copy(date = date)
    }

    fun setTime(hour: Int, minute: Int) {
        uiState = uiState.copy(hour = hour, minute = minute)
    }
}


data class UiState(
    val id: Int? = null,
    val comment: String = "",
    val date: Long = System.currentTimeMillis(),
    val hour: Int = LocalTime.now().hour,
    val minute: Int = LocalTime.now().minute,
    val canEdit: Boolean = false
    )