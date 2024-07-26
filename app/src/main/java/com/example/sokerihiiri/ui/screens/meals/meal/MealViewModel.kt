package com.example.sokerihiiri.ui.screens.meals.meal

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.Meal
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.dateAndTimeToUTCLong
import com.example.sokerihiiri.utils.timestampToHoursAndMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

class InvalidCaloriesException(message: String) : Exception(message)
class InvalidCarbohydratesException(message: String) : Exception(message)

@HiltViewModel
class MealViewModel @Inject constructor(
    private val repository: SokerihiiriRepository
) : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    fun setCalories(calories: Int) {
        uiState = uiState.copy(calories = calories, caloriesError = null)
    }

    fun setCarbs(carbs: Int) {
        uiState = uiState.copy(carbohydrates = carbs, carbohydratesError = null)
    }

    fun setComment(comment: String) {
        uiState = uiState.copy(comment = comment)
    }

    fun setTime(hour: Int, minute: Int) {
        uiState = uiState.copy(hour = hour, minute = minute)
    }

    fun setDate(date: Long) {
        uiState = uiState.copy(date = date)
    }

    fun resetState() {
        uiState = UiState()
    }

    fun saveMeal() {
        try {
            validateFields()
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute,
            )
            val meal = Meal(
                calories = uiState.calories,
                carbohydrates = uiState.carbohydrates,
                timestamp = dateTime,
                comment = uiState.comment
            )
            viewModelScope.launch {
                repository.insertMeal(meal)
            }
            resetState()
        } catch (e: InvalidCaloriesException) {
            uiState = uiState.copy(caloriesError = e.message)
            throw e
        } catch (e: InvalidCarbohydratesException) {
            uiState = uiState.copy(carbohydratesError = e.message)
            throw e
        }
        catch (e: Exception) {
            Log.e("MealViewModel", "Error saving meal", e)
        }
    }

    fun getMealById(id: String?) {
        if (id.toString() == uiState.id.toString()) return
        if (id == null) {
            resetState()
            return
        }

        viewModelScope.launch {
            val meal = repository.getMealById(id)
            val (hour, minute) = timestampToHoursAndMinutes(meal.timestamp)
            uiState = UiState(
                id = meal.id,
                calories = meal.calories,
                carbohydrates = meal.carbohydrates,
                hour = hour,
                minute = minute,
                date = meal.timestamp,
                comment = meal.comment
            )
        }
    }

    fun updateMeal() {
        Log.d("MealViewModel", "Updating meal")
        try {
            validateFields()
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute,
            )
            val meal = Meal(
                id = uiState.id!!,
                calories = uiState.calories,
                carbohydrates = uiState.carbohydrates,
                timestamp = dateTime,
                comment = uiState.comment
            )
            viewModelScope.launch {
                Log.d("MealViewModel", "Updating meal LAUNCH")
                Log.d("MealViewModel", "Meal: $meal")
                repository.updateMeal(meal)
            }
        } catch (e: InvalidCaloriesException) {
            uiState = uiState.copy(caloriesError = e.message)
            throw e
        } catch (e: InvalidCarbohydratesException) {
            uiState = uiState.copy(carbohydratesError = e.message)
            throw e
        } catch (e: Exception) {
            Log.e("MealViewModel", "Error updating meal", e)
        }
    }

    fun deleteMealById() {
        try {
            viewModelScope.launch {
                repository.deleteMealById(uiState.id!!)
            }
        } catch (e: Exception) {
            Log.e("MealViewModel", "Error deleting meal", e)
        }
    }

    private fun validateFields() {
        if (uiState.calories <= 0) {
            throw InvalidCaloriesException("Kcal oltava suurempi kuin 0")
        }
        if (uiState.carbohydrates <= 0) {
            throw InvalidCarbohydratesException("Hiilarien oltava suurempi kuin 0")
        }
    }

    fun setCanEdit(canEdit: Boolean) {
        uiState = uiState.copy(canEdit = canEdit)
    }
}

data class UiState (
    val id: Int? = null,
    val calories: Int = 0,
    val carbohydrates: Int = 0,
    val hour: Int = LocalTime.now().hour,
    val minute: Int = LocalTime.now().minute,
    val date: Long = System.currentTimeMillis(),
    val comment: String = "",
    val caloriesError: String? = null,
    val carbohydratesError: String? = null,
    val canEdit: Boolean = false
)