package com.pokemon.pokemonbuilder.domain

import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.TypesQuery

data class Pokemon(
    val id: Int,
    val pokemonId: Int,
    val name: String,
    val nickname: String = name,
    val nature: NaturesQuery.Pokemon_v2_nature,
    val item: ItemsQuery.Pokemon_v2_item,
    val pokeball: ItemsQuery.Pokemon_v2_item,
    val stats: HashMap<String,Int>,
    val moves: List<PokemonQuery.Pokemon_v2_pokemonmofe>,
    val pokemonType: List<PokemonQuery.Pokemon_v2_pokemontype>,
    val teraType: TypesQuery.Pokemon_v2_type,
    val ability: PokemonQuery.Pokemon_v2_pokemonability,
    val level: Int,
    val gender: String,
    val shiny: Boolean,
    val ev: HashMap<String,Int>,
    val iv: HashMap<String,Int>
)
