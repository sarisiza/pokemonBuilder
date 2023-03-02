package com.pokemon.pokemonbuilder.di

import android.content.Context
import android.net.ConnectivityManager
import com.custom_libs_spil.network_connection.utils.NetworkState
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.usecases.DexUseCases
import com.pokemon.pokemonbuilder.usecases.GetItemsListUseCase
import com.pokemon.pokemonbuilder.usecases.GetMovesListUseCase
import com.pokemon.pokemonbuilder.usecases.GetPokemonListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun providesConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun providesDexUseCases(
        networkRepository: NetworkRepository,
        networkState: NetworkState
    ): DexUseCases =
        DexUseCases(
            GetPokemonListUseCase(networkRepository,networkState),
            GetMovesListUseCase(networkRepository,networkState),
            GetItemsListUseCase(networkRepository,networkState)
        )

}