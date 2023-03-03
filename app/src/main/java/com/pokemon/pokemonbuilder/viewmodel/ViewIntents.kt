package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.entities.PokemonDb
import com.pokemon.pokemonbuilder.entities.PokemonTeamDb

sealed class ViewIntents{

    object SIGN_UP: ViewIntents()

    object PICK_LANGUAGE: ViewIntents()

    data class GET_POKEMON(val generation: Int): ViewIntents()

    object GET_MOVES: ViewIntents()

    object GET_ITEMS: ViewIntents()

    data class SAVE_POKEMON(val pokemon: PokemonDb): ViewIntents()

    data class SAVE_TEAM(val team: PokemonTeamDb): ViewIntents()

    object ADD_POKEMON: ViewIntents()

    object ADD_TEAM: ViewIntents()

    object VIEW_CREATED_POKEMON: ViewIntents()

    object VIEW_CREATED_TEAMS: ViewIntents()

}
