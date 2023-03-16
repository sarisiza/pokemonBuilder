package com.pokemon.pokemonbuilder.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController
import com.pokemon.pokemonbuilder.utils.DatabaseAction
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents

@Composable
fun TeamsInfo(
    builderViewModel: BuilderViewModel,
    navController: NavController,
    headerSize: TextUnit
) {
    builderViewModel.getIntent(ViewIntents.VIEW_CREATED_TEAMS)
    Column {
        when(val states = builderViewModel.createdTeams.collectAsState().value){
            is UIState.ERROR -> {
                ShowErrorDialog(e = states.e) {
                    builderViewModel.getIntent(ViewIntents.VIEW_CREATED_TEAMS)
                }
            }
            UIState.LOADING -> {
                LoadingScreen()
            }
            is UIState.SUCCESS -> {
                PokemonList(
                    pokemonItems = states.response,
                    key = "teams-list",
                    headerSize = headerSize,
                    selectedItem = {
                        builderViewModel.selectedTeam = it
                        navController.navigate(DexScreens.TEAMS_DETAIL.route)
                    },
                    swipeLeft = {
                        builderViewModel.getIntent(ViewIntents.TEAM_OPERATION(it,DatabaseAction.DELETE))
                    }, //delete
                    swipeRight = {
                        navController.navigate(DexScreens.EDIT_TEAM.route)
                    } //edit
                )
            }
        }
    }
}