package com.pokemon.pokemonbuilder.di

import com.pokemon.pokemonbuilder.database.LocalRepository
import com.pokemon.pokemonbuilder.database.LocalRepositoryImpl
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.service.NetworkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesNetworkRepository(
        networkRepositoryImpl: NetworkRepositoryImpl
    ): NetworkRepository

    @Binds
    abstract fun providesLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository

}