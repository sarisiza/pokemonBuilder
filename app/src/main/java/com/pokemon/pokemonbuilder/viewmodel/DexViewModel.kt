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

    var changeLanguage = false
    var selectedPokemon: PokemonQuery.Pokemon_v2_pokemon? = null
    var selectedItemInfo: ItemsQuery.Pokemon_v2_item? = null
    var selectedGeneration: GenerationEnum = GenerationEnum.GEN_IX

    fun getIntent(intent: ViewIntents){
        when(intent){
            is ViewIntents.GET_POKEMON -> {
                getPokemonList(intent.language,intent.generation)
            }
            is ViewIntents.GET_ITEMS -> {
                getItemsList(intent.language)
            }
            else -> {}
        }
    }

    private fun getPokemonList(language: LanguageEnum, generation: Int){
        safeViewModelScope.launch {
            dexUseCases.getPokemonList(language,generation).collect{
                _pokemonList.value = it
            }
        }
    }

    private fun getItemsList(language: LanguageEnum){
        safeViewModelScope.launch {
            dexUseCases.getItemsList(language).collect{
                _itemsList.value = it
            }
        }
    }

    fun updateBackTrack(backTrack: Boolean){
        _hasBackTrack.value = backTrack
    }

}