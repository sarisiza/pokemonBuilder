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

    private val _firstTimeLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val fistTimeLogged: StateFlow<Boolean> get() = _firstTimeLogged

    fun getIntent(intents: ViewIntents){
        when(intents){
            ViewIntents.CHECK_FIRST_TIME -> {
                checkFirstTime()
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
            else -> {}
        }
    }

    fun getLanguage(language: LanguageEnum? = null){
        safeViewModelScope.launch {
            if(language == null){
                _appLanguage.value = loginUseCases.getLanguageUseCase()
            } else{
                _appLanguage.value = loginUseCases.languageUseCase(language)
            }
        }
    }

    fun signUp(user: User? = null){
        safeViewModelScope.launch {
            if(user == null){
                _loggedUser.value = loginUseCases.getLoginInfoUseCase()
            } else{
                _loggedUser.value = loginUseCases.signUpUseCase(user.firstName,user.lastName)
            }
        }
    }

    fun checkFirstTime(){
        safeViewModelScope.launch {
            _firstTimeLogged.value = loginUseCases.checkIfFirstTimeUseCase()
        }
    }

}