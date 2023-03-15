package com.pokemon.pokemonbuilder.ui.views

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
import com.pokemon.pokemonbuilder.viewmodel.BuilderViewModel
import com.pokemon.pokemonbuilder.viewmodel.ViewIntents
import java.util.concurrent.TimeUnit

@Composable
fun CreateTeamName(
    builderViewModel: BuilderViewModel,
    headerSize: TextUnit,
    titleSize: TextUnit,
    textSize: TextUnit,
    navController: NavHostController,
    action: DatabaseAction,
) {
    var teamName by remember { mutableStateOf("Team") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val maxChar = 20
        val focusManager = LocalFocusManager.current
        Text(
            text = stringResource(R.string.label_create_team),
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
                    teamName = it.take(maxChar)
                    if(it.length>maxChar) focusManager.clearFocus()
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
                        id = TimeUnit.MILLISECONDS.toSeconds(System.nanoTime()).toInt(),
                        name = teamName,
                        pokemon = mutableListOf()
                    )
                    builderViewModel.getIntent(ViewIntents.TEAM_OPERATION(team,action))
                    //navController.navigate()
                } else if(action == DatabaseAction.UPDATE){
                    builderViewModel.selectedTeam?.let {
                        builderViewModel.getIntent(ViewIntents.TEAM_OPERATION(it,action,teamName))
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