package com.example.sokerihiiri.ui.screens.browse.meals


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.LocalBrowseViewModel
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModel
import com.example.sokerihiiri.utils.timestampToDateTimeString

@Composable
fun BrowseMealsScreen(
    navController: NavController
) {
    val browseViewModel = LocalBrowseViewModel.current
    val allMeals by browseViewModel.allMeals.observeAsState(emptyList())

    Column {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(text = "Pvm. ja aika")
            Text(text = "Kcal")
            Text(text = "Hiilarit")
        }
        Divider()
        LazyColumn {
            items(allMeals) { meal ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("${Screens.Browse.Meals.Meal.route}/${meal.id}")
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = timestampToDateTimeString(meal.timestamp), Modifier.weight(0.50f))
                    Text(text = meal.calories.toString(), Modifier.weight(0.2f),textAlign = TextAlign.End)
                    Text(text = meal.carbohydrates.toString(), Modifier.weight(0.2f),textAlign = TextAlign.End)
                }
            }
        }
    }

}