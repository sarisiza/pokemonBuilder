package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.pokemon.pokemonbuilder.R

@Composable
fun ShowErrorDialog(
    e: Exception,
    retry: () -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    if(openDialog.value){
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = stringResource(R.string.alert_title)) },
            text = {Text(text = e.localizedMessage?: stringResource(R.string.alert_unexpected))},
            dismissButton = {
                Button(onClick = {openDialog.value = false}) {
                    Text(text = stringResource(R.string.alert_dismiss))
                }
            },
            confirmButton = {
                Button(onClick = { retry() }) {
                    Text(text = stringResource(R.string.alert_retry))
                }
            }
        )
    }
}