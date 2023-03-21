package com.pokemon.pokemonbuilder.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.pokemon.pokemonbuilder.domain.Pokemon

@Entity(tableName = "pokemon")
data class PokemonDb(
    @PrimaryKey val id: Int,
    val pokemonId: Int,
    val name: String,
    val nickname: String,
    val nature: String,
    val item: String,
    val pokeball: String,
    val stats: String,
    val moves: String,
    val pokemonType: String,
    val teraType: String,
    val ability: String,
    val level: Int,
    val gender: Int,
    val shiny: Boolean,
    val ev: String,
    val iv: String,
    val inTeam: Boolean
)

fun Pokemon.mapToDatabase(): PokemonDb{
    val gson = Gson()
    return PokemonDb(
        this.id,
        this.pokemonId,
        this.name,
        this.nickname,
        gson.toJson(this.nature),
        gson.toJson(this.item),
        gson.toJson(this.pokeball),
        gson.toJson(this.stats),
        gson.toJson(this.moves),
        gson.toJson(this.pokemonType),
        gson.toJson(this.teraType),
        gson.toJson(this.ability),
        this.level,
        this.gender.gender,
        this.shiny,
        gson.toJson(this.ev),
        gson.toJson(this.iv),
        this.inTeam
    )
}
