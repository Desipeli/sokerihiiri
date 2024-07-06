package com.example.sokerihiiri.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ViewAndEdit(
    modifier: Modifier = Modifier,
    navController: NavController,
    id: String?,
    allowEdit: MutableState<Boolean>,
    save: () -> Unit,
    update: () -> Unit,
    delete: () -> Unit,
    content: @Composable () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        content()
        if (allowEdit.value) {
            if (id == null) {
                TextButton(modifier = Modifier
                    .align(Alignment.BottomEnd),
                    enabled = allowEdit.value,
                    onClick = {
                        save()
                        navController.navigateUp()
                    }) {
                    Text("Tallenna")
                }
            } else {
                TextButton(modifier = Modifier
                    .align(Alignment.BottomEnd),
                    enabled = allowEdit.value,
                    onClick = {
                        update()
                        navController.navigateUp()
                    }) {
                    Text("Tallenna muutokset")
                }
            }
        } else {
            TextButton(modifier = Modifier
                .align(Alignment.BottomEnd),
                onClick = {
                    allowEdit.value = true
                }) {
                Text("Muokkaa")
            }
        }
        if (id != null) {
            TextButton(modifier = Modifier
                .align(Alignment.BottomStart),
                onClick = {
                    showDeleteDialog = true
                }) {
                Text("Poista")
            }
        }
        if (showDeleteDialog && id != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Poista") },
                text = { Text("Haluatko varmasti poistaa tiedot?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            delete()
                            navController.navigateUp()
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Poista")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("Peruuta")
                    }
                }
            )
        }
    }
}