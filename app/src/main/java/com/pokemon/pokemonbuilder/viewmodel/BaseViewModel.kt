package com.pokemon.pokemonbuilder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemon.pokemonbuilder.BuildConfig
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.plus
import javax.inject.Inject

private const val TAG = "BaseViewModel"

open class BaseViewModel: ViewModel() {

    protected val _appLanguage: MutableStateFlow<LanguageEnum> = MutableStateFlow(LanguageEnum.ENG)
    val appLanguage: StateFlow<LanguageEnum> get() = _appLanguage

    protected val _loggedUser: MutableStateFlow<User> = MutableStateFlow(User("",""))
    val loggedUser: StateFlow<User> get() = _loggedUser

    protected val safeViewModelScope by lazy {
        viewModelScope + Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler{ _, e->
            if(BuildConfig.DEBUG){
                Log.e(TAG, "Error in viewmodel scope: ${e.localizedMessage}", e)
            }
        }
    }

}