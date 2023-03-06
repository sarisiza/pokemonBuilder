package com.pokemon.pokemonbuilder.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

class CheckIfUserUseCase(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(): Boolean{
        var loggedIn = false
        dataStore.data.collect{
            loggedIn = it[booleanPreferencesKey("LOGGED_IN")]?:false
        }
        return loggedIn
    }

}