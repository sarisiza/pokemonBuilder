package com.pokemon.pokemonbuilder.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.TypesQuery
import com.pokemon.pokemonbuilder.database.entities.PokemonDb
import com.pokemon.pokemonbuilder.utils.GenderEnum
import com.pokemon.pokemonbuilder.utils.createRandomId
import com.pokemon.pokemonbuilder.utils.getGender
import com.pokemon.pokemonbuilder.utils.replaceFirstCap

data class Pokemon(
    val id: Int,
    val pokemonId: Int,
    val name: String,
    var nickname: String,
    var nature: NaturesQuery.Pokemon_v2_nature? = null,
    var item: ItemsQuery.Pokemon_v2_item? = null,
    var pokeball: ItemsQuery.Pokemon_v2_item? = null,
    var stats: HashMap<String,Int>? = null,
    var moves: List<PokemonQuery.Pokemon_v2_pokemonmofe>? = null,
    val pokemonType: List<PokemonQuery.Pokemon_v2_pokemontype>,
    var teraType: TypesQuery.Pokemon_v2_type? = null,
    var ability: PokemonQuery.Pokemon_v2_pokemonability? = null,
    var level: Int = 0,
    var gender: GenderEnum,
    var shiny: Boolean = false,
    var ev: HashMap<String,Int>? = null,
    var iv: HashMap<String,Int>? = null,
    var inTeam: Boolean = false
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
            pokemonDb.gender.getGender(),
            pokemonDb.shiny,
            gson.fromJson<HashMap<String,Int>>(pokemonDb.ev,HashMap::class.java),
            gson.fromJson<HashMap<String,Int>>(pokemonDb.iv,HashMap::class.java),
            pokemonDb.inTeam
        )
    }
}

fun PokemonQuery.Pokemon_v2_pokemon?.mapToCreatedPokemon(): Pokemon {
    var gender = GenderEnum.MALE
    this?.pokemon_v2_pokemonspecy?.gender_rate?.let {
        if(it<0) gender = GenderEnum.GENDERLESS
    }
    return Pokemon(
        id = createRandomId(),
        pokemonId = this?.id?:0,
        name = this?.name?.replaceFirstCap() ?:"",
        nickname = this?.name?.replaceFirstCap() ?:"",
        pokemonType = this?.pokemon_v2_pokemontypes?: emptyList(),
        gender = gender
    )
}