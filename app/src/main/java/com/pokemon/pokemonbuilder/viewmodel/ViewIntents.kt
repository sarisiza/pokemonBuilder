package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.database.entities.PokemonDb
import com.pokemon.pokemonbuilder.database.entities.PokemonTeamDb
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.DatabaseAction
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User

sealed class ViewIntents{

    data class SIGN_UP(
        val user: User
        ): ViewIntents()

    data class PICK_LANGUAGE(
        val language: LanguageEnum
        ): ViewIntents()

    object GET_LANGUAGE: ViewIntents()

    object GET_USER: ViewIntents()

    object CHECK_FIRST_TIME_LANGUAGE: ViewIntents()

    object CHECK_FIRST_TIME_USER: ViewIntents()

    data class GET_POKEMON(
        val language: LanguageEnum,
        val generation: Int? = null,
        val pokemonName: String? = null,
        val pokemonId: Int? = null,
        val type: String? = null
        ): ViewIntents()

    data class GET_ITEMS(
        val language: LanguageEnum
        ): ViewIntents()

    data class GET_NATURES(
        val language: LanguageEnum
        ): ViewIntents()

    data class GET_TYPES(
        val language: LanguageEnum
        ): ViewIntents()

    data class POKEMON_OPERATION(
        val pokemon: Pokemon,
        val action: DatabaseAction
        ): ViewIntents()

    data class TEAM_OPERATION(
        val team: PokemonTeam,
        val action: DatabaseAction,
        val name: String? = null
    ): ViewIntents()

    data class POKEMON_IN_TEAM_OPERATION(
        val team: PokemonTeam,
        val pokemon: Pokemon,
        val action: DatabaseAction
        ): ViewIntents()

    object VIEW_CREATED_POKEMON: ViewIntents()

    object VIEW_CREATED_TEAMS: ViewIntents()

    data class GET_POKEMON_IN_TEAM(
        val team: PokemonTeam
        ): ViewIntents()

}
