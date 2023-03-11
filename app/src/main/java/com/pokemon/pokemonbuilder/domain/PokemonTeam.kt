package com.pokemon.pokemonbuilder.domain

data class PokemonTeam(
    val id: Int,
    val name: String = "Team $id",
    val pokemon: MutableList<Pokemon>
)
