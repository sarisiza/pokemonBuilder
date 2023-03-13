package com.pokemon.pokemonbuilder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pokemon.pokemonbuilder.database.entities.PokemonDb
import com.pokemon.pokemonbuilder.database.entities.PokemonTeamDb

@Database(
    entities = [
        PokemonDb::class,
        PokemonTeamDb::class
    ],
    version = 1
)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun getPokemonDAO(): PokemonDAO

}