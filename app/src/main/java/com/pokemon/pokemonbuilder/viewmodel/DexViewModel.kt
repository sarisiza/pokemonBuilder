package com.pokemon.pokemonbuilder.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.usecases.DexUseCases
import com.pokemon.pokemonbuilder.usecases.LoginUseCases
import com.pokemon.pokemonbuilder.utils.GenerationEnum
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.UIState
import com.pokemon.pokemonbuilder.utils.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DexViewModel @Inject constructor(
    private val dexUseCases: DexUseCases,
    private val loginUseCases: LoginUseCases
): BaseViewModel() {

    private val _pokemonList: MutableStateFlow<UIState<List<PokemonQuery.Pokemon_v2_pokemon>>> =
        MutableStateFlow(UIState.LOADING)
    val pokemonList:
            StateFlow<UIState<List<PokemonQuery.Pokemon_v2_pokemon>>>
        get() = _pokemonList

    private val _itemsList: MutableStateFlow<UIState<List<ItemsQuery.Pokemon_v2_item>>> =
        MutableStateFlow(UIState.LOADING)
    val itemsList:
            StateFlow<UIState<List<ItemsQuery.Pokemon_v2_item>>>
        get() = _itemsList

    private val _hasBackTrack: MutableState<Boolean> = mutableStateOf(false)
    val hasBackTrack: State<Boolean> get() = _hasBackTrack

    private val mAppLanguage: MutableState<LanguageEnum> = mutableStateOf(LanguageEnum.ENG)
    val appLanguage: State<LanguageEnum> get() = mAppLanguage

    private val mLoggedUser: MutableState<User?> = mutableStateOf(null)
    val loggedUser: State<User?> get() = mLoggedUser

    private val _firstTimeLanguage: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val fistTimeLanguage: StateFlow<Boolean?> get() = _firstTimeLanguage

    private val _firstTimeUser: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val firstTimeUser: StateFlow<Boolean?> get() = _firstTimeUser

    private var accessJob: Job? = null
    private var accessJob2: Job? = null

    var changeLanguage = false
    var selectedPokemon: PokemonQuery.Pokemon_v2_pokemon? = null
    var selectedItemInfo: ItemsQuery.Pokemon_v2_item? = null
    var selectedGeneration: GenerationEnum = GenerationEnum.GEN_IX

    fun getIntent(intent: ViewIntents){
        when(intent){
            is ViewIntents.GET_POKEMON -> {
                getPokemonList(intent.generation)
            }
            ViewIntents.GET_ITEMS -> {
                getItemsList()
            }
            ViewIntents.CHECK_FIRST_TIME_LANGUAGE -> {
                checkFirstTimeLanguage()
            }
            is ViewIntents.PICK_LANGUAGE -> {
                getLanguage(intent.language)
            }
            is ViewIntents.SIGN_UP -> {
                signUp(intent.user)
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

    private fun getPokemonList(generation: Int){
        safeViewModelScope.launch {
            dexUseCases.getPokemonList(appLanguage.value,generation).collect{
                _pokemonList.value = it
            }
        }
    }

    private fun getItemsList(){
        safeViewModelScope.launch {
            dexUseCases.getItemsList(appLanguage.value).collect{
                _itemsList.value = it
            }
        }
    }

    fun updateBackTrack(backTrack: Boolean){
        _hasBackTrack.value = backTrack
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
                    mLoggedUser.value?.firstName = it[stringPreferencesKey("FIRST_NAME")]?:""
                    mLoggedUser.value?.lastName = it[stringPreferencesKey("LAST_NAME")]?:""
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
            }
        }
    }

}