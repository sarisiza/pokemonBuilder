package com.pokemon.pokemonbuilder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemon.pokemonbuilder.BuildConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus

private const val TAG = "BaseViewModel"

open class BaseViewModel: ViewModel() {

    protected val safeViewModelScope by lazy {
        viewModelScope + Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler{ _, e->
            if(BuildConfig.DEBUG){
                Log.e(TAG, "Error in viewmodel scope: ${e.localizedMessage}", e)
            }
        }
    }

}