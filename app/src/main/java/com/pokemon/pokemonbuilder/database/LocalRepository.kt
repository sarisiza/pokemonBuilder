package com.pokemon.pokemonbuilder.database

import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam

interface LocalRepository {

    //Pokemon Functions
    suspend fun addPokemon(pokemon: Pokemon)

    suspend fun removePokemon(pokemon: Pokemon)

    suspend fun getAllPokemon(): List<Pokemon>

    suspend fun modifyPokemon(pokemon: Pokemon)

    suspend fun getPokemonFromId(id: Int): Pokemon

    //Team Functions
    suspend fun addTeam(pokemonTeam: PokemonTeam)

    suspend fun deleteTeam(pokemonTeam: PokemonTeam)

    suspend fun getAllTeams(): List<PokemonTeam>

    suspend fun modifyPokemonTeam(pokemonTeam: PokemonTeam)

}