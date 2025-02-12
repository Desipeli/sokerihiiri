package com.example.sokerihiiri.ui.screens.meals


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sokerihiiri.R
import com.example.sokerihiiri.ui.LocalBrowseViewModel
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.utils.timestampToDateTimeString

@Composable
fun BrowseMealsScreen(
    navController: NavController
) {
    // Näkymä ateriatietojen selailuun

    val browseViewModel = LocalBrowseViewModel.current
    val allMeals by browseViewModel.allMeals.observeAsState(emptyList())

    Column {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(text = stringResource(R.string.date_and_time_title))
            Text(text = stringResource(R.string.kcal))
            Text(text = stringResource(R.string.carbs))
        }
        Divider()
        LazyColumn {
            items(allMeals) { meal ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("${Screens.Meals.EditMeal.route}/${meal.id}")
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = timestampToDateTimeString(meal.timestamp), Modifier.weight(0.50f))
                    Text(text = meal.calories.toString(), Modifier.weight(0.2f),textAlign = TextAlign.End)
                    Text(text = meal.carbohydrates.toString(), Modifier.weight(0.2f),textAlign = TextAlign.End)
                    if (meal.comment != "") {
                        Image(
                            painter = painterResource(id = R.drawable.comment),
                            contentDescription = stringResource(R.string.comment))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}