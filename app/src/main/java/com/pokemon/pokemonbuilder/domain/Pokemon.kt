package com.pokemon.pokemonbuilder.domain

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.TypesQuery
import com.pokemon.pokemonbuilder.database.entities.PokemonDb
import com.pokemon.pokemonbuilder.utils.*

data class Pokemon(
    val id: Int,
    val pokemonId: Int,
    val name: String,
    var nickname: String,
    var nature: NaturesQuery.Pokemon_v2_nature? = null,
    var item: ItemsQuery.Pokemon_v2_item? = null,
    var pokeball: ItemsQuery.Pokemon_v2_item? = null,
    val baseStats: HashMap<PokemonQuery.Pokemon_v2_stat,Int>,
    var stats: HashMap<String,Int>,
    var allMoves: List<PokemonQuery.Pokemon_v2_pokemonmofe>,
    var moves: MutableList<PokemonQuery.Pokemon_v2_pokemonmofe>,
    val pokemonType: List<PokemonQuery.Pokemon_v2_pokemontype>,
    var teraType: TypesQuery.Pokemon_v2_type? = null,
    val abilityList: List<PokemonQuery.Pokemon_v2_pokemonability>,
    var ability: PokemonQuery.Pokemon_v2_pokemonability?,
    var level: Int = 0,
    var gender: GenderEnum,
    var shiny: Boolean = false,
    var ev: HashMap<String,Int>,
    var iv: HashMap<String,Int>,
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
            gson.fromJson<HashMap<PokemonQuery.Pokemon_v2_stat,Int>>(pokemonDb.stats,HashMap::class.java),
            gson.fromJson<HashMap<String,Int>>(pokemonDb.baseStats,HashMap::class.java),
            gson.fromJson<List<PokemonQuery.Pokemon_v2_pokemonmofe>>(pokemonDb.allMoves, List::class.java),
            gson.fromJson<MutableList<PokemonQuery.Pokemon_v2_pokemonmofe>>(pokemonDb.moves, MutableList::class.java),
            gson.fromJson<List<PokemonQuery.Pokemon_v2_pokemontype>>(pokemonDb.pokemonType, List::class.java),
            gson.fromJson(pokemonDb.teraType, TypesQuery.Pokemon_v2_type::class.java),
            gson.fromJson<List<PokemonQuery.Pokemon_v2_pokemonability>>(pokemonDb.abilityList,List::class.java),
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
    val statMap: HashMap<String,Int> = hashMapOf()
    val baseStatMap: HashMap<PokemonQuery.Pokemon_v2_stat,Int> = hashMapOf()
    val addMap: HashMap<String,Int> = hashMapOf()
    val moveList: MutableList<PokemonQuery.Pokemon_v2_pokemonmofe> = mutableListOf()
    this?.pokemon_v2_pokemonmoves?.let {moves ->
        for(i in 0..3){
            moveList.add(moves[i])
        }
    }
    this?.pokemon_v2_pokemonstats?.let {statList ->
        statList.forEach {item ->
            item.pokemon_v2_stat?.let {
                baseStatMap[it] = item.base_stat
                statMap[it.pokemon_v2_statnames[0].name] =
                    calculateTotalStats(
                        stat = it.id,
                        base = item.base_stat,
                        iv = 0,
                        ev = 0
                    )
                addMap[it.pokemon_v2_statnames[0].name] = 0
            }
        }
    }
    return Pokemon(
        id = createRandomId(),
        pokemonId = this?.id?:0,
        name = this?.name?.replaceFirstCap() ?:"",
        nickname = this?.name?.replaceFirstCap() ?:"",
        pokemonType = this?.pokemon_v2_pokemontypes?: emptyList(),
        allMoves = this?.pokemon_v2_pokemonmoves?: emptyList(),
        moves = moveList,
        level = 100,
        gender = gender,
        baseStats = baseStatMap,
        stats = statMap,
        iv = addMap,
        ev = addMap,
        ability = this?.pokemon_v2_pokemonabilities?.get(0),
        abilityList = this?.pokemon_v2_pokemonabilities?: emptyList()
    )
}