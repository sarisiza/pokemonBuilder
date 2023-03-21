package com.pokemon.pokemonbuilder.ui.views

import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pokemon.pokemonbuilder.R
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.DatabaseAction
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.utils.createRandomId
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import java.util.concurrent.TimeUnit

private const val TAG = "CreateTeam"
@Composable
fun CreateTeamName(
    builderViewModel: BuilderViewModel,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    navController: NavHostController,
    action: DatabaseAction,
) {
    Log.d(TAG, "CreateTeamName: Composing")
    var teamName by remember { mutableStateOf(builderViewModel.selectedTeam?.name?:"Team") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val focusManager = LocalFocusManager.current
        val instruction =
            if(action == DatabaseAction.ADD)
                R.string.label_create_team
            else R.string.label_edit_team
        Text(
            text = stringResource(instruction),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            fontSize = headerSize,
            fontWeight = FontWeight.Bold
        )
        Row {
            Text(
                text = stringResource(R.string.label_team_name),
                modifier = Modifier
                    .weight(20F)
                    .align(Alignment.CenterVertically)
                    .padding(5.dp),
                fontSize = titleSize
            )
            OutlinedTextField(
                value = teamName,
                onValueChange = {
                    teamName = it
                },
                label = { Text(text = stringResource(R.string.label_enter_team_name))},
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
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = {
                if(action == DatabaseAction.ADD){
                    val team = PokemonTeam(
                        id = createRandomId(),
                        name = teamName,
                        pokemon = mutableListOf()
                    )
                    builderViewModel.getIntent(ViewIntents.TEAM_OPERATION(team,action))
                    builderViewModel.selectedTeam = team
                    //navController.navigate(DexScreens.SEARCH_POKEMON.route)
                    when(builderViewModel.databaseOperationDone.value){
                        is UIState.ERROR -> {}
                        UIState.LOADING -> {}
                        is UIState.SUCCESS -> navController.navigate(DexScreens.TEAMS_LIST.route)
                    }
                }
                if(action == DatabaseAction.UPDATE){
                    builderViewModel.selectedTeam?.let {
                        builderViewModel.getIntent(ViewIntents.TEAM_OPERATION(it,action,teamName))
                    }
                    when(builderViewModel.databaseOperationDone.value){
                        is UIState.ERROR -> {}
                        UIState.LOADING -> {}
                        is UIState.SUCCESS -> navController.navigate(DexScreens.TEAMS_LIST.route)
                    }
                }
            }
        ) {
            Text(
                text = stringResource(R.string.label_save_team),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(5.dp),
                fontSize = textSize
            )
        }
    }

}