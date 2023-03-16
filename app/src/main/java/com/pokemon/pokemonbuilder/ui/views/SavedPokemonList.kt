package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents

@Composable
fun CreatedPokemonList(
    builderViewModel: BuilderViewModel,
    navController: NavController,
    titleSize: TextUnit,
    textSize: TextUnit,
    selectedTeam: PokemonTeam? = null
) {
    if(selectedTeam == null){
        builderViewModel.getIntent(ViewIntents.VIEW_CREATED_POKEMON)
    } else {
        builderViewModel.getIntent(ViewIntents.GET_POKEMON_IN_TEAM(selectedTeam))
    }
    Column {
        when(val state = builderViewModel.createdPokemon.collectAsState().value){
            is UIState.ERROR -> {
                ShowErrorDialog(e = state.e) {
                    if(selectedTeam == null){
                        builderViewModel.getIntent(ViewIntents.VIEW_CREATED_POKEMON)
                    } else {
                        builderViewModel.getIntent(ViewIntents.GET_POKEMON_IN_TEAM(selectedTeam))
                    }
                }
            }
            UIState.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            is UIState.SUCCESS -> {
                var key: String
                if(selectedTeam == null){
                    key = "Created Pokemon"
                } else {
                    key = selectedTeam.name
                }
                PokemonList(
                    pokemonItems = state.response,
                    key = key,
                    titleSize = titleSize,
                    textSize = textSize,
                    selectedItem = {
                        builderViewModel.selectedPokemon = it
                        navController.navigate(DexScreens.SAVED_POKEMON_DETAILS.route)
                    },
                    swipeLeft = {},
                    swipeRight = {}
                )
            }
        }
    }
}