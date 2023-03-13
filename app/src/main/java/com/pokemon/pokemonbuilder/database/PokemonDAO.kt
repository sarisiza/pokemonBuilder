package com.pokemon.pokemonbuilder.database

import androidx.room.*
import com.pokemon.pokemonbuilder.database.entities.PokemonDb
import com.pokemon.pokemonbuilder.database.entities.PokemonTeamDb

@Dao
interface PokemonDAO {

    //Team queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(vararg team: PokemonTeamDb)

    @Delete
    suspend fun deleteTeam(vararg team: PokemonTeamDb)

    @Query("SELECT * FROM pokemon-team")
    suspend fun getPokemonTeams(): List<PokemonTeamDb>

    @Query("SELECT * FROM pokemon-team WHERE id = :id")
    suspend fun getTeamById(id: Int): PokemonTeamDb

    @Update
    suspend fun updateTeam(vararg team: PokemonTeamDb)

    //Pokemon queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(vararg pokemon: PokemonDb)

    @Delete
    suspend fun deletePokemon(vararg pokemon: PokemonDb)

    @Query("SELECT * FROM pokemon")
    suspend fun getPokemon(): List<PokemonDb>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonDb

    @Update
    suspend fun updatePokemon(vararg pokemon: PokemonDb)

}