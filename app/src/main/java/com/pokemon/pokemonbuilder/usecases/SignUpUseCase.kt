package com.pokemon.pokemonbuilder.usecases

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pokemon.pokemonbuilder.utils.User
import javax.inject.Inject

private const val TAG = "SignUpUseCase"
class SignUpUseCase @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
    ): User {
        dataStore.edit {
            it[stringPreferencesKey("FIRST_NAME")] = firstName
            it[stringPreferencesKey("LAST_NAME")] = lastName
            it[booleanPreferencesKey("LOGGED_IN")] = true
        }
        return User(firstName,lastName)
    }

}