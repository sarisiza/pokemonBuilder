package com.pokemon.pokemonbuilder.database

import com.pokemon.pokemonbuilder.database.entities.mapToDatabase
import com.pokemon.pokemonbuilder.database.entities.mapToTeamDatabase
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.domain.mapToPokemon
import com.pokemon.pokemonbuilder.domain.mapToTeam
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val pokemonDAO: PokemonDAO
): LocalRepository {

    override suspend fun addPokemon(pokemon: Pokemon) {
        val pokemonDb = pokemon.mapToDatabase()
        pokemonDAO.insertPokemon(pokemonDb)
    }

    override suspend fun removePokemon(pokemon: Pokemon) {
        val pokemonDb = pokemon.mapToDatabase()
        pokemonDAO.deletePokemon(pokemonDb)
    }

    override suspend fun getAllPokemon(): List<Pokemon> {
        val allPokemon = pokemonDAO.getPokemon()
        return allPokemon.mapToPokemon()
    }

    override suspend fun modifyPokemon(pokemon: Pokemon) {
        val pokemonDb = pokemon.mapToDatabase()
        pokemonDAO.updatePokemon(pokemonDb)
    }

    override suspend fun getPokemonFromId(id: Int): Pokemon {
        val pokemonDb = listOf(pokemonDAO.getPokemonById(id))
        return pokemonDb.mapToPokemon()[0]
    }

    override suspend fun addTeam(pokemonTeam: PokemonTeam) {
        val pokemonTeamDb = pokemonTeam.mapToTeamDatabase()
        pokemonDAO.insertTeam(pokemonTeamDb)
    }

    override suspend fun deleteTeam(pokemonTeam: PokemonTeam) {
        val pokemonTeamDb = pokemonTeam.mapToTeamDatabase()
        pokemonDAO.deleteTeam(pokemonTeamDb)
    }

    override suspend fun getAllTeams(): List<PokemonTeam> {
        val allTeams = pokemonDAO.getPokemonTeams()
        return allTeams.mapToTeam()
    }

    override suspend fun modifyPokemonTeam(pokemonTeam: PokemonTeam) {
        val pokemonTeamDb = pokemonTeam.mapToTeamDatabase()
        pokemonDAO.updateTeam(pokemonTeamDb)
    }


}