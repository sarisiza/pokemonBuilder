package com.pokemon.pokemonbuilder.domain

import com.google.gson.Gson
import com.pokemon.pokemonbuilder.database.entities.PokemonTeamDb

data class PokemonTeam(
    val id: Int,
    var name: String = "Team $id",
    val pokemon: MutableList<Int>
)

fun List<PokemonTeamDb>.mapToTeam(): List<PokemonTeam>{
    val gson = Gson()
    return this.map {
        PokemonTeam(
            it.id,
            it.name,
            gson.fromJson<MutableList<Int>>(it.pokemon, MutableList::class.java)
        )
    }
}
