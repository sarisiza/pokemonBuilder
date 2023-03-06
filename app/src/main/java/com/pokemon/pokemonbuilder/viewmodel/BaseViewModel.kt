package com.pokemon.pokemonbuilder.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

    protected val mAppLanguage: MutableState<LanguageEnum> = mutableStateOf(LanguageEnum.ENG)
    val appLanguage: State<LanguageEnum> get() = mAppLanguage

    protected val mLoggedUser: MutableState<User> = mutableStateOf(User("",""))
    val loggedUser: State<User> get() = mLoggedUser

    protected val safeViewModelScope by lazy {
        viewModelScope + Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler{ _, e->
            if(BuildConfig.DEBUG){
                Log.e(TAG, "Error in viewmodel scope: ${e.localizedMessage}", e)
            }
        }
    }

}