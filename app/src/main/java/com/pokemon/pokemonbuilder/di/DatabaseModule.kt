package com.pokemon.pokemonbuilder.di

import android.content.Context
import androidx.room.Room
import com.pokemon.pokemonbuilder.database.PokemonDAO
import com.pokemon.pokemonbuilder.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providesPokemonDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemonDatabase"
        ).build()

    @Provides
    fun providesPokemonDao(
        pokemonDatabase: PokemonDatabase
    ): PokemonDAO = pokemonDatabase.getPokemonDAO()

}