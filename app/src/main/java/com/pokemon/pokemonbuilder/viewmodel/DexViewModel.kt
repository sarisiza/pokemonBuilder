package com.pokemon.pokemonbuilder.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.TypesQuery
import com.pokemon.pokemonbuilder.usecases.DexUseCases
import com.pokemon.pokemonbuilder.utils.GenerationEnum
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DexViewModel @Inject constructor(
    private val dexUseCases: DexUseCases
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

    private val _naturesList: MutableStateFlow<UIState<List<NaturesQuery.Pokemon_v2_nature>>> =
        MutableStateFlow(UIState.LOADING)
    val naturesList:
            StateFlow<UIState<List<NaturesQuery.Pokemon_v2_nature>>> get() = _naturesList

    private val _typesList: MutableStateFlow<UIState<List<TypesQuery.Pokemon_v2_type>>> =
        MutableStateFlow(UIState.LOADING)
    val typesList:
            StateFlow<UIState<List<TypesQuery.Pokemon_v2_type>>> get() = _typesList

    private val _hasBackTrack: MutableState<Boolean> = mutableStateOf(false)
    val hasBackTrack: State<Boolean> get() = _hasBackTrack

    var selectedPokemon: PokemonQuery.Pokemon_v2_pokemon? = null
    var selectedGeneration: GenerationEnum = GenerationEnum.GEN_IX
    var selectedType: String? = null
    var selectedPokemonId: Int? = null
    var searchForName: String? = null

    fun getIntent(intent: ViewIntents){
        when(intent){
            is ViewIntents.GET_POKEMON -> {
                getPokemonList(
                    intent.language,
                    intent.generation,
                    intent.pokemonName,
                    intent.pokemonId,
                    intent.type
                )
            }
            is ViewIntents.GET_ITEMS -> {
                getItemsList(intent.language)
            }
            is ViewIntents.GET_NATURES -> {
                getNaturesList(intent.language)
            }
            is ViewIntents.GET_TYPES -> {
                getTypesList(intent.language)
            }
            else -> {}
        }
    }

    private fun getTypesList(language: LanguageEnum) {
        safeViewModelScope.launch {
            dexUseCases.getTypesList(language).collect{
                _typesList.value = it
            }
        }
    }

    private fun getNaturesList(language: LanguageEnum) {
        safeViewModelScope.launch {
            dexUseCases.getNaturesList(language).collect{
                _naturesList.value = it
            }
        }
    }

    private fun getPokemonList(
        language: LanguageEnum,
        generation: Int?,
        pokemonName: String?,
        pokemonId: Int?,
        type: String?
    ){
        safeViewModelScope.launch {
            dexUseCases.getPokemonList(
                language,
                generation,
                pokemonName,
                pokemonId,
                type
            ).collect{
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