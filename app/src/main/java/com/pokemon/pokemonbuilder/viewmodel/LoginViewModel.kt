package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.usecases.LoginUseCases
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
): BaseViewModel() {

    private val _firstTimeLanguage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val fistTimeLanguage: StateFlow<Boolean> get() = _firstTimeLanguage

    private val _firstTimeUser: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val firstTimeUser: StateFlow<Boolean> get() = _firstTimeUser

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
                _appLanguage.value = loginUseCases.getLanguageUseCase()
            } else{
                _appLanguage.value = loginUseCases.languageUseCase(language)
            }
        }
    }

    private fun signUp(user: User? = null){
        safeViewModelScope.launch {
            if(user == null){
                _loggedUser.value = loginUseCases.getLoginInfoUseCase()
            } else{
                _loggedUser.value = loginUseCases.signUpUseCase(user.firstName,user.lastName)
            }
        }
    }

    private fun checkFirstTimeLanguage(){
        safeViewModelScope.launch {
            _firstTimeLanguage.value = loginUseCases.checkIfLanguageUseCase()
        }
    }

    private fun checkFirstTimeUser(){
        safeViewModelScope.launch {
            _firstTimeUser.value = loginUseCases.checkIfUserUseCase()
        }
    }

}