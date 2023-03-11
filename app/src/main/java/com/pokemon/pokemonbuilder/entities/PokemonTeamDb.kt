package com.pokemon.pokemonbuilder.entities

import androidx.room.Entity

@Entity
data class PokemonTeamDb(
    val id: Int,
    val name: String,
    val pokemon: String,
)
