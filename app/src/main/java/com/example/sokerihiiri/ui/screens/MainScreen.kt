package com.example.sokerihiiri.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.example.sokerihiiri.ui.navigation.Screens

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
        Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
            StyledTextButton(
                onClick = { navController.navigate(Screens.Measurements.Main.route)},
                content = stringResource(id = Screens.Measurements.Main.titleResourceId)
            )
            StyledTextButton(
                onClick = { navController.navigate(Screens.Injections.Main.route) },
                content = stringResource(id = Screens.Injections.Main.titleResourceId))

            StyledTextButton(
                onClick = { navController.navigate(Screens.Meals.Main.route) },
                content = stringResource(id = Screens.Meals.Main.titleResourceId))
            StyledTextButton(
                onClick = { navController.navigate(Screens.Others.Main.route) },
                text = stringResource(id = Screens.Others.Main.titleResourceId))
    }
}