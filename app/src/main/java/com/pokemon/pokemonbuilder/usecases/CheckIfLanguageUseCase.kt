package com.pokemon.pokemonbuilder.usecases

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

private const val TAG = "CheckIfLanguageUseCase"
class CheckIfLanguageUseCase(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(): Boolean{
        var langPicked = false
        dataStore.data.collect{
            langPicked = it[booleanPreferencesKey("LANGUAGE_PICKED")]?:false
            Log.d(TAG, "picked: $langPicked")
        }
        return langPicked
    }

}