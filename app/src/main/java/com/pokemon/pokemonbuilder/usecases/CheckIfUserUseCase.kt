package com.pokemon.pokemonbuilder.usecases

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

private const val TAG = "CheckIfUserUseCase"
class CheckIfUserUseCase(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(): Boolean{
        var loggedIn = false
        dataStore.data.collect{
            loggedIn = it[booleanPreferencesKey("LOGGED_IN")]?:false
            Log.d(TAG, "logged: $loggedIn")
        }
        return loggedIn
    }

}