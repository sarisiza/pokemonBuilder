package com.pokemon.pokemonbuilder.viewmodel

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pokemon.pokemonbuilder.usecases.LoginUseCases
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
): BaseViewModel() {

    private val _firstTimeLanguage: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val fistTimeLanguage: StateFlow<Boolean?> get() = _firstTimeLanguage

    private val _firstTimeUser: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val firstTimeUser: StateFlow<Boolean?> get() = _firstTimeUser

    private var accessJob: Job? = null
    private var accessJob2: Job? = null


    fun getIntent(intents: ViewIntents){
        when(intents){
            ViewIntents.CHECK_FIRST_TIME_LANGUAGE -> {
                checkFirstTimeLanguage()
            }
            is ViewIntents.PICK_LANGUAGE -> {
                getLanguage(intents.language)
            }
            is ViewIntents.SIGN_UP -> {
                signUp(intents.user)
            }
            ViewIntents.GET_LANGUAGE -> {
                getLanguage()
            }
            ViewIntents.GET_USER -> {
                signUp()
            }
            ViewIntents.CHECK_FIRST_TIME_USER -> {
                checkFirstTimeUser()
            }
            else -> {}
        }
    }

    private fun getLanguage(language: LanguageEnum? = null){
        accessJob?.cancel()
        accessJob = null
        safeViewModelScope.launch {
            if(language == null){
                loginUseCases.getDatastoreUseCase().collect{
                    val langId = it[intPreferencesKey("LANGUAGE")]
                    when(langId){
                        7 -> mAppLanguage.value = LanguageEnum.ESP
                        9 -> mAppLanguage.value = LanguageEnum.ENG
                    }
                }
            } else{
                mAppLanguage.value = loginUseCases.languageUseCase(language)
            }
        }
    }

    private fun signUp(user: User? = null){
        accessJob2?.cancel()
        accessJob2 = null
        safeViewModelScope.launch {
            if(user == null){
                loginUseCases.getDatastoreUseCase().collect{
                    mLoggedUser.value.firstName = it[stringPreferencesKey("FIRST_NAME")]?:""
                    mLoggedUser.value.lastName = it[stringPreferencesKey("LAST_NAME")]?:""
                }
            } else{
                mLoggedUser.value = loginUseCases.signUpUseCase(user.firstName,user.lastName)
            }
        }
    }

    private fun checkFirstTimeLanguage(){
        if(accessJob == null){
            accessJob = safeViewModelScope.launch {
                loginUseCases.getDatastoreUseCase().collect{
                    _firstTimeLanguage.value = it[booleanPreferencesKey("LANGUAGE_PICKED")]?:false
                }
            }
        }
    }

    private fun checkFirstTimeUser(){
        if(accessJob2 == null) {
            accessJob2 = safeViewModelScope.launch {
                loginUseCases.getDatastoreUseCase().collect{
                    _firstTimeUser.value = it[booleanPreferencesKey("LOGGED_IN")]?:false
                }
                Log.d(TAG, "checkFirstTimeUser: ${_firstTimeUser.value}")
            }
        }
    }

}