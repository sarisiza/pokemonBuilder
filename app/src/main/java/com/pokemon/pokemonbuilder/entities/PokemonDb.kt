package com.pokemon.pokemonbuilder.entities

import androidx.room.Entity

@Entity
data class PokemonDb(
    val id: Int,
    val pokemonId: Int,
    val name: String,
    val nickname: String = name,
    val nature: String,
    val item: String,
    val pokeball: String,
    val stats: String,
    val moves: String,
    val pokemonType: String,
    val teraType: String,
    val ability: String,
    val level: Int,
    val gender: String,
    val shiny: Boolean,
    val ev: String,
    val iv: String
)
