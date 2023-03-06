package com.pokemon.pokemonbuilder.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.custom_libs_spil.network_connection.services.ServiceCall
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun providesDexUseCases(
        networkRepository: NetworkRepository,
        serviceCall: ServiceCall
    ): DexUseCases =
        DexUseCases(
            GetPokemonListUseCase(networkRepository,serviceCall),
            GetMovesListUseCase(networkRepository,serviceCall),
            GetItemsListUseCase(networkRepository,serviceCall)
        )

    @Provides
    fun providesLoginUseCases(
        dataStore: DataStore<Preferences>
    ): LoginUseCases =
        LoginUseCases(
            SignUpUseCase(dataStore),
            PickLanguageUseCase(dataStore),
            GetLoginInfoUseCase(dataStore),
            GetLanguageUseCase(dataStore),
            CheckIfLanguageUseCase(dataStore),
            CheckIfUserUseCase(dataStore)
        )

}