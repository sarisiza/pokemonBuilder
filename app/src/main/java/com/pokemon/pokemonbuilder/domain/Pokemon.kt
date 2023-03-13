package com.pokemon.pokemonbuilder.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.TypesQuery
import com.pokemon.pokemonbuilder.database.entities.PokemonDb

data class Pokemon(
    val id: Int,
    val pokemonId: Int,
    val name: String,
    var nickname: String,
    var nature: NaturesQuery.Pokemon_v2_nature,
    var item: ItemsQuery.Pokemon_v2_item,
    var pokeball: ItemsQuery.Pokemon_v2_item,
    var stats: HashMap<String,Int>,
    var moves: List<PokemonQuery.Pokemon_v2_pokemonmofe>,
    val pokemonType: List<PokemonQuery.Pokemon_v2_pokemontype>,
    var teraType: TypesQuery.Pokemon_v2_type,
    var ability: PokemonQuery.Pokemon_v2_pokemonability,
    var level: Int,
    var gender: String,
    var shiny: Boolean,
    var ev: HashMap<String,Int>,
    var iv: HashMap<String,Int>,
    var inTeam: Boolean
)

fun List<PokemonDb>.mapToPokemon(): List<Pokemon>{
    val gson = Gson()
    return this.map {pokemonDb ->
        Pokemon(
            pokemonDb.id,
            pokemonDb.pokemonId,
            pokemonDb.name,
            pokemonDb.nickname,
            gson.fromJson(pokemonDb.nature,NaturesQuery.Pokemon_v2_nature::class.java),
            gson.fromJson(pokemonDb.item,ItemsQuery.Pokemon_v2_item::class.java),
            gson.fromJson(pokemonDb.pokeball,ItemsQuery.Pokemon_v2_item::class.java),
            gson.fromJson<HashMap<String,Int>>(pokemonDb.stats,HashMap::class.java),
            gson.fromJson<List<PokemonQuery.Pokemon_v2_pokemonmofe>>(pokemonDb.moves, List::class.java),
            gson.fromJson<List<PokemonQuery.Pokemon_v2_pokemontype>>(pokemonDb.pokemonType, List::class.java),
            gson.fromJson(pokemonDb.teraType, TypesQuery.Pokemon_v2_type::class.java),
            gson.fromJson(pokemonDb.ability, PokemonQuery.Pokemon_v2_pokemonability::class.java),
            pokemonDb.level,
            pokemonDb.gender,
            pokemonDb.shiny,
            gson.fromJson<HashMap<String,Int>>(pokemonDb.ev,HashMap::class.java),
            gson.fromJson<HashMap<String,Int>>(pokemonDb.iv,HashMap::class.java),
            pokemonDb.inTeam
        )
    }
}
