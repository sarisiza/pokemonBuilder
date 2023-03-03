package com.pokemon.pokemonbuilder.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(): LanguageEnum{
        var langId = 9
        dataStore.data.collect{
            langId = it[intPreferencesKey("LANGUAGE")] ?: 9
        }
        return when(langId){
            7 -> LanguageEnum.ESP
            9 -> LanguageEnum.ENG
            else -> LanguageEnum.ENG
        }
    }

}