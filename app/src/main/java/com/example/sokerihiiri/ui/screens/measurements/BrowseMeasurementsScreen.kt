package com.example.sokerihiiri.ui.screens.measurements

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.R
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.ui.screens.BrowseViewModel
import com.example.sokerihiiri.utils.minutesToHoursAndMinutes
import com.example.sokerihiiri.utils.timestampToDateTimeString

@Composable
fun BrowseMeasurementsScreen(
    navController: NavController,
) {

    val browseViewModel: BrowseViewModel = hiltViewModel()
    val allMeasurements by browseViewModel.allMeasurements.observeAsState(emptyList())


    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            Text(text = stringResource(R.string.date_and_time_title))
            Text(text = stringResource(R.string.value))
            Text(text = stringResource(R.string.after_meal))

        }
        Divider()
        LazyColumn {
            items(allMeasurements) { measurement ->
                val (hours, minutes) = minutesToHoursAndMinutes(measurement.minutesFromMeal)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("${Screens.Measurements.EditMeasurement.route}/${measurement.id}")
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = timestampToDateTimeString(measurement.timestamp),
                        Modifier.weight(0.50f)
                    )
                    Text(
                        text = measurement.value.toString(),
                        Modifier.weight(0.2f),
                        textAlign = TextAlign.End
                    )
                    if (measurement.afterMeal) {
                        Text(
                            text = "$hours ${stringResource(R.string.h)} $minutes ${stringResource(R.string.min)}",
                            Modifier.weight(0.40f),
                            textAlign = TextAlign.End
                        )
                    } else {
                        Text(text = "", Modifier.weight(0.40f))
                    }
                    if (measurement.comment != "") {
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


