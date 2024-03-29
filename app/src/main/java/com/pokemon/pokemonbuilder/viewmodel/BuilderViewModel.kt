package com.pokemon.pokemonbuilder.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.usecases.BuilderUseCases
import com.pokemon.pokemonbuilder.utils.DatabaseAction
import com.pokemon.pokemonbuilder.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuilderViewModel @Inject constructor(
    private val builderUseCases: BuilderUseCases
): BaseViewModel() {

    private val _createdTeams: MutableStateFlow<UIState<List<PokemonTeam>>> =
        MutableStateFlow(UIState.LOADING)
    val createdTeams: StateFlow<UIState<List<PokemonTeam>>> get() = _createdTeams

    private val _createdPokemon: MutableStateFlow<UIState<List<Pokemon>>> =
        MutableStateFlow(UIState.LOADING)
    val createdPokemon: StateFlow<UIState<List<Pokemon>>> get() = _createdPokemon

    private val _databaseOperationDone: MutableState<UIState<Boolean>> =
        mutableStateOf(UIState.LOADING)
    val databaseOperationDone: State<UIState<Boolean>> get() = _databaseOperationDone

    var selectedTeam: PokemonTeam? = null
    var selectedPokemon: Pokemon? = null

    val shouldCreate = mutableStateOf(false)
    val createTeam = mutableStateOf(true) //false if creating pokemon

    fun getIntent(intent: ViewIntents){
        when(intent){
            is ViewIntents.GET_POKEMON_IN_TEAM -> {
                getPokemonInTeam(intent.team)
            }
            is ViewIntents.POKEMON_IN_TEAM_OPERATION -> {
                modifyPokemonInTeam(intent.team,intent.pokemon,intent.action)
            }
            is ViewIntents.POKEMON_OPERATION -> {
                modifyCreatedPokemon(intent.pokemon,intent.action)
            }
            is ViewIntents.TEAM_OPERATION -> {
                modifyTeam(intent.team,intent.action,intent.name)
            }
            ViewIntents.VIEW_CREATED_POKEMON -> {
                getAllPokemon()
            }
            ViewIntents.VIEW_CREATED_TEAMS -> {
                getAllTeams()
            }
            else -> {}
        }
    }

    private fun getAllTeams() {
        safeViewModelScope.launch {
            builderUseCases.getTeams().collect{
                _createdTeams.value = it
            }
        }
    }

    private fun getAllPokemon() {
        safeViewModelScope.launch {
            builderUseCases.getCreatedPokemon().collect{
                _createdPokemon.value = it
            }
        }
    }

    private fun modifyTeam(team: PokemonTeam, action: DatabaseAction, name: String?) {
        safeViewModelScope.launch {
            when(action){
                DatabaseAction.ADD -> {
                    builderUseCases.createTeam(team).collect{
                        _databaseOperationDone.value = it
                    }
                }
                DatabaseAction.UPDATE -> {
                    name?.let {
                        builderUseCases.modifyTeamName(team,name).collect{
                            _databaseOperationDone.value = it
                        }
                    }
                }
                DatabaseAction.DELETE -> {
                    builderUseCases.deleteTeam(team).collect{
                        _databaseOperationDone.value = it
                    }
                }
            }
        }
    }

    private fun modifyCreatedPokemon(pokemon: Pokemon, action: DatabaseAction) {
        safeViewModelScope.launch {
            when(action){
                DatabaseAction.ADD -> {
                    builderUseCases.createNewPokemon(pokemon).collect{
                        _databaseOperationDone.value = it
                    }
                }
                DatabaseAction.UPDATE -> {
                    builderUseCases.modifyPokemon(pokemon).collect{
                        _databaseOperationDone.value = it
                    }
                }
                DatabaseAction.DELETE -> {
                    builderUseCases.deletePokemon(pokemon).collect{
                        _databaseOperationDone.value = it
                    }
                }
            }
        }
    }

    private fun modifyPokemonInTeam(team: PokemonTeam, pokemon: Pokemon, action: DatabaseAction) {
        safeViewModelScope.launch {
            when(action){
                DatabaseAction.ADD -> {
                    builderUseCases.addPokemonToTeam(team,pokemon).collect{
                        _databaseOperationDone.value = it
                    }
                }
                DatabaseAction.UPDATE -> {}
                DatabaseAction.DELETE -> {
                    builderUseCases.removePokemonFromTeam(team,pokemon).collect{
                        _databaseOperationDone.value = it
                    }
                }
            }
        }
    }

    private fun getPokemonInTeam(team: PokemonTeam) {
        safeViewModelScope.launch {
            builderUseCases.getPokemonInTeam(team).collect{
                _createdPokemon.value = it
            }
        }
    }


}