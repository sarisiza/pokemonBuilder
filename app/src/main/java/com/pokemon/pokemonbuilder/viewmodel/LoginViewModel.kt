package com.pokemon.pokemonbuilder.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

    private val mLoggedUser: MutableState<User?> = mutableStateOf(null)
    val loggedUser: State<User?> get() = mLoggedUser

    private val mAppLanguage: MutableState<LanguageEnum?> = mutableStateOf(null)
    val appLanguage: State<LanguageEnum?> get() = mAppLanguage

    private var accessJob: Job? = null
    private var accessJob2: Job? = null
    private var accessJob3: Job? = null
    private var accessJob4: Job? = null


    fun getIntent(intents: ViewIntents){
        when(intents){
            ViewIntents.CHECK_FIRST_TIME_LANGUAGE -> {
                checkFirstTimeLanguage()
            }
            is ViewIntents.PICK_LANGUAGE -> {
                pickLanguage(intents.language)
            }
            is ViewIntents.SIGN_UP -> {
                signUp(intents.user)
            }
            ViewIntents.GET_LANGUAGE -> {
                getLanguage()
            }
            ViewIntents.GET_USER -> {
                getUser()
            }
            ViewIntents.CHECK_FIRST_TIME_USER -> {
                checkFirstTimeUser()
            }
            else -> {}
        }
    }

    private fun pickLanguage(language: LanguageEnum){
        accessJob?.cancel()
        accessJob = null
        accessJob3?.cancel()
        accessJob3 = null
        safeViewModelScope.launch {
            loginUseCases.languageUseCase(language)
        }
    }

    private fun signUp(user: User){
        accessJob2?.cancel()
        accessJob2 = null
        accessJob4?.cancel()
        accessJob4 = null
        safeViewModelScope.launch {
            loginUseCases.signUpUseCase(user.firstName,user.lastName)
        }
    }

    private fun checkFirstTimeLanguage(){
        if(accessJob3 == null){
            accessJob3 = safeViewModelScope.launch {
                loginUseCases.getDatastoreUseCase().collect{
                    _firstTimeLanguage.value = it[booleanPreferencesKey("LANGUAGE_PICKED")]?:false
                }
            }
        }
    }

    private fun checkFirstTimeUser(){
        if(accessJob4 == null) {
            accessJob4 = safeViewModelScope.launch {
                loginUseCases.getDatastoreUseCase().collect{
                    _firstTimeUser.value = it[booleanPreferencesKey("LOGGED_IN")]?:false
                }
                Log.d(TAG, "checkFirstTimeUser: ${_firstTimeUser.value}")
            }
        }
    }

    private fun getUser(){
        if(accessJob2 == null){
            accessJob2 = safeViewModelScope.launch {
                loginUseCases.getDatastoreUseCase().collect{
                    val loggedUser = User(it[stringPreferencesKey("FIRST_NAME")]?:"",it[stringPreferencesKey("LAST_NAME")]?:"")
                    mLoggedUser.value = loggedUser
                }
            }
        }
    }

    private fun getLanguage(){
        if(accessJob == null){
            accessJob = safeViewModelScope.launch {
                loginUseCases.getDatastoreUseCase().collect{
                    when(it[intPreferencesKey("LANGUAGE")]){
                        7 -> mAppLanguage.value = LanguageEnum.ESP
                        9 -> mAppLanguage.value = LanguageEnum.ENG
                    }
                }
            }
        }
    }

}