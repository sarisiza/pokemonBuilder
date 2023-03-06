package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.entities.PokemonDb
import com.pokemon.pokemonbuilder.entities.PokemonTeamDb
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User

sealed class ViewIntents{

    data class SIGN_UP(val user: User): ViewIntents()

    data class PICK_LANGUAGE(val language: LanguageEnum): ViewIntents()

    object GET_LANGUAGE: ViewIntents()

    object GET_USER: ViewIntents()

    object CHECK_FIRST_TIME_LANGUAGE: ViewIntents()

    object CHECK_FIRST_TIME_USER: ViewIntents()

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
