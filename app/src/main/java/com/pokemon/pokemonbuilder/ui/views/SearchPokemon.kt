package com.pokemon.pokemonbuilder.ui.views

import android.view.KeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.utils.QueryType
import com.pokemon.pokemonbuilder.viewmodel.DexViewModel
import com.pokemon.pokemonbuilder.viewmodel.LoginViewModel

@Composable
fun SearchPokemon(
    dexViewModel: DexViewModel,
    headerSize: TextUnit,
    textSize: TextUnit,
    loginViewModel: LoginViewModel,
    navController: NavController,
) {
    var pokemonName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var search by remember { mutableStateOf(false) }
    Column {
        Row {
            OutlinedTextField(
                value = pokemonName,
                onValueChange = {
                    pokemonName = it
                },
                label = { Text(text = stringResource(R.string.label_enter_pokemon)) },
                enabled = true,
                singleLine = true,
                modifier = Modifier
                    .weight(60F)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusManager.clearFocus()
                            true
                        }
                        false
                    }
            )
            Button(
                onClick = {
                    dexViewModel.searchForName = pokemonName
                    search = true
                }
            ) {
                Text(
                    text = stringResource(R.string.btn_search),
                    fontSize = textSize,
                )
            }
        }
        PokemonInfo(
            dexViewModel = dexViewModel,
            loginViewModel = loginViewModel,
            navController = navController,
            headerSize = headerSize,
            textSize = textSize,
            queryType = QueryType.NAME(search)
        )
        if(search) search = false
    }
}