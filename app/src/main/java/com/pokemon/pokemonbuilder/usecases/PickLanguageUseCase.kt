package com.pokemon.pokemonbuilder.usecases

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.pokemon.pokemonbuilder.utils.Language
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import javax.inject.Inject

private const val TAG = "PickLanguageUseCase"
class PickLanguageUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
){

    suspend operator fun invoke(
        language: LanguageEnum
    ): LanguageEnum{
        dataStore.edit {
            it[intPreferencesKey("LANGUAGE")] = language.language.id
            it[booleanPreferencesKey("LANGUAGE_PICKED")] = true
        }
        Log.d(TAG, "language: ${language.language.name}")
        return language
    }

}