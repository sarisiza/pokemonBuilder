package com.pokemon.pokemonbuilder.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.custom_libs_spil.network_connection.utils.NetworkState
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

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

    @Provides
    @Singleton
    fun providesLoginUseCases(
        dataStore: DataStore<Preferences>
    ): LoginUseCases =
        LoginUseCases(
            SignUpUseCase(dataStore),
            PickLanguageUseCase(dataStore),
            GetLoginInfoUseCase(dataStore),
            GetLanguageUseCase(dataStore)
        )

}