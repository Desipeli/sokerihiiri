package com.example.sokerihiiri.ui.screens.browse.measurements

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
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModel
import com.example.sokerihiiri.utils.minutesToHoursAndMinutes
import com.example.sokerihiiri.utils.timestampToDateTimeString

@Composable
fun BrowseMeasurementsScreen(
    browseViewModel: BrowseViewModel,
) {

    val allMeasurements by browseViewModel.allMeasurements.observeAsState(emptyList())

    Column {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(text = "Pvm. ja aika")
            Text(text = "Arvo")
            Text(text = "Aterian jälkeen")

        }
        Divider()
        LazyColumn {
            items(allMeasurements) { measurement ->
                val (hours, minutes) = minutesToHoursAndMinutes(measurement.minutesFromMeal)
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = timestampToDateTimeString(measurement.timestamp), Modifier.weight(0.50f))
                    Text(text = measurement.value.toString(), Modifier.weight(0.2f),textAlign = TextAlign.End)
                    if (measurement.afterMeal) {
                        Text(text =  "$hours h $minutes min", Modifier.weight(0.40f),textAlign = TextAlign.End)
                    } else {
                        Text(text = "", Modifier.weight(0.40f))
                    }
                }
            }
        }
    }

}