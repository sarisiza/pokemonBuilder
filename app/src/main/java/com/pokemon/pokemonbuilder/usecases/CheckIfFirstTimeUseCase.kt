package com.pokemon.pokemonbuilder.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

class CheckIfFirstTimeUseCase(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(): Boolean{
        var langPicked = false
        dataStore.data.collect{
            langPicked = it[booleanPreferencesKey("LANGUAGE_PICKED")]?:false
        }
        return langPicked
    }

}