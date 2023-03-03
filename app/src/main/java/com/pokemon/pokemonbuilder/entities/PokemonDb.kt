package com.pokemon.pokemonbuilder.entities

import com.pokemon.pokemonbuilder.PokemonQuery

data class PokemonDb(
    val id: Int,
    val name: String,
    val abilities: List<PokemonQuery.Pokemon_v2_ability>,
    val availableMoves: List<PokemonQuery.Pokemon_v2_pokemonmofe>,
    val types: List<PokemonQuery.Pokemon_v2_pokemontype>,
    val sprite: String
)
