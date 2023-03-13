package com.pokemon.pokemonbuilder.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.custom_libs_spil.network_connection.services.ServiceCall
import com.pokemon.pokemonbuilder.database.LocalRepository
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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
            GetItemsListUseCase(networkRepository,serviceCall)
        )

    @Provides
    fun providesLoginUseCases(
        dataStore: DataStore<Preferences>
    ): LoginUseCases =
        LoginUseCases(
            SignUpUseCase(dataStore),
            PickLanguageUseCase(dataStore),
            GetDatastoreUseCase(dataStore)
        )

    @Provides
    fun providesBuilderUseCases(
        localRepository: LocalRepository
    ): BuilderUseCases =
        BuilderUseCases(
            GetTeamsUseCase(localRepository),
            GetCreatedPokemonUseCase(localRepository),
            CreateNewPokemonUseCase(localRepository),
            DeletePokemonUseCase(localRepository),
            ModifyPokemonUseCase(localRepository),
            AddPokemonToTeamUseCase(localRepository),
            RemovePokemonFromTeamUseCase(localRepository),
            GetPokemonInTeamUseCase(localRepository),
            ModifyTeamNameUseCase(localRepository)
        )

}