package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.MovesQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.usecases.DexUseCases
import com.pokemon.pokemonbuilder.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DexViewModel @Inject constructor(
    private val dexUseCases: DexUseCases
): BaseViewModel() {

    private val _movesList: MutableStateFlow<UIState<List<MovesQuery.Pokemon_v2_move>>> =
        MutableStateFlow(UIState.LOADING)
    val movesList:
            StateFlow<UIState<List<MovesQuery.Pokemon_v2_move>>> get() = _movesList

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

    var selectedPokemon: PokemonQuery.Pokemon_v2_pokemon? = null
    var selectedMoveInfo: MovesQuery.Pokemon_v2_move? = null
    var selectedItemInfo: ItemsQuery.Pokemon_v2_item? = null

    fun getIntent(intent: ViewIntents){
        when(intent){
            is ViewIntents.GET_POKEMON -> {
                getPokemonList(intent.generation)
            }
            ViewIntents.GET_ITEMS -> {
                getItemsList()
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

}