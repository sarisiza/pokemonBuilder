package com.pokemon.pokemonbuilder.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.pokemon.pokemonbuilder.usecases.LoginUseCases
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
): BaseViewModel() {

    private val _firstTimeLanguage: MutableState<Boolean?> = mutableStateOf(false)
    val fistTimeLanguage: State<Boolean?> get() = _firstTimeLanguage

    private val _firstTimeUser: MutableState<Boolean?> = mutableStateOf(false)
    val firstTimeUser: State<Boolean?> get() = _firstTimeUser

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
        safeViewModelScope.launch {
            if(language == null){
                mAppLanguage.value = loginUseCases.getLanguageUseCase()
            } else{
                mAppLanguage.value = loginUseCases.languageUseCase(language)
            }
        }
    }

    private fun signUp(user: User? = null){
        safeViewModelScope.launch {
            if(user == null){
                mLoggedUser.value = loginUseCases.getLoginInfoUseCase()
            } else{
                mLoggedUser.value = loginUseCases.signUpUseCase(user.firstName,user.lastName)
            }
        }
    }

    private fun checkFirstTimeLanguage(){
        safeViewModelScope.launch {
            _firstTimeLanguage.value = loginUseCases.checkIfLanguageUseCase()
            Log.d(TAG, "checkFirstTimeLanguage: ${_firstTimeLanguage.value}")
        }
    }

    private fun checkFirstTimeUser(){
        safeViewModelScope.launch {
            _firstTimeUser.value = loginUseCases.checkIfUserUseCase()
            Log.d(TAG, "checkFirstTimeUser: ${_firstTimeUser.value}")
        }
    }

}