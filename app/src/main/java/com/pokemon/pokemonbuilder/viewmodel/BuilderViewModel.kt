package com.pokemon.pokemonbuilder.viewmodel

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

    private val _pokemonInTeam: MutableStateFlow<UIState<List<Pokemon>>> =
        MutableStateFlow(UIState.LOADING)
    val pokemonInTeam: StateFlow<UIState<List<Pokemon>>> get() = _pokemonInTeam


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
        when(action){
            DatabaseAction.ADD -> {
                builderUseCases.createTeam(team)
            }
            DatabaseAction.UPDATE -> {
                name?.let {
                    builderUseCases.modifyTeamName(team,name)
                }
            }
            DatabaseAction.DELETE -> {
                builderUseCases.deleteTeam(team)
            }
        }
    }

    private fun modifyCreatedPokemon(pokemon: Pokemon, action: DatabaseAction) {
        when(action){
            DatabaseAction.ADD -> {
                builderUseCases.createNewPokemon(pokemon)
            }
            DatabaseAction.UPDATE -> {
                builderUseCases.modifyPokemon(pokemon)
            }
            DatabaseAction.DELETE -> {
                builderUseCases.deletePokemon(pokemon)
            }
        }
    }

    private fun modifyPokemonInTeam(team: PokemonTeam, pokemon: Pokemon, action: DatabaseAction) {
        when(action){
            DatabaseAction.ADD -> {
                builderUseCases.addPokemonToTeam(team,pokemon)
            }
            DatabaseAction.UPDATE -> {}
            DatabaseAction.DELETE -> {
                builderUseCases.removePokemonFromTeam(team,pokemon)
            }
        }
    }

    private fun getPokemonInTeam(team: PokemonTeam) {
        builderUseCases.getPokemonInTeam(team)
    }


}