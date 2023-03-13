package com.pokemon.pokemonbuilder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.pokemon.pokemonbuilder.domain.PokemonTeam

@Entity(tableName = "pokemon-team")
data class PokemonTeamDb(
    @PrimaryKey val id: Int,
    val name: String,
    val pokemon: String,
)

fun PokemonTeam.mapToTeamDatabase(): PokemonTeamDb{
    val gson = Gson()
    return PokemonTeamDb(
        this.id,
        this.name,
        gson.toJson(this.pokemon)
    )
}
