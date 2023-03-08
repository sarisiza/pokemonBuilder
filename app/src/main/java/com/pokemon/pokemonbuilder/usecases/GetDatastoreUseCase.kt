package com.pokemon.pokemonbuilder.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

private const val TAG = "CheckIfLanguageUseCase"
class GetDatastoreUseCase(
    private val dataStore: DataStore<Preferences>
) {

    operator fun invoke(): Flow<Preferences> {
        return dataStore.data
    }

}