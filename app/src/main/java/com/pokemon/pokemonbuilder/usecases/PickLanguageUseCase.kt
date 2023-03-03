package com.pokemon.pokemonbuilder.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import javax.inject.Inject

class PickLanguageUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
){

    suspend operator fun invoke(
        language: LanguageEnum
    ){
        dataStore.edit {
            it[intPreferencesKey("LANGUAGE")] = language.language.id
            it[booleanPreferencesKey("LANGUAGE_PICKED")] = true
        }
    }

}