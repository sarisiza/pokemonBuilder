package com.pokemon.pokemonbuilder.usecases

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pokemon.pokemonbuilder.utils.User
import javax.inject.Inject

class GetLoginInfoUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(): User{
        val user = User("","")
        dataStore.data.collect{
            user.firstName = it[stringPreferencesKey("FIRST_NAME")]?:""
            user.lastName = it[stringPreferencesKey("LAST_NAME")]?:""
        }
        return user
    }

}