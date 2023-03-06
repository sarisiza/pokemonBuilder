package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.usecases.CreatePokemonUseCases
import com.pokemon.pokemonbuilder.usecases.DexUseCases
import com.pokemon.pokemonbuilder.usecases.PokemonUseCasesParent
import com.pokemon.pokemonbuilder.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class PokemonViewModel(
    private val pokemonUseCases: PokemonUseCasesParent
): BaseViewModel() {

    protected val _pokemonList: MutableStateFlow<UIState<List<PokemonQuery.Pokemon_v2_pokemon>>> =
        MutableStateFlow(UIState.LOADING)
    val pokemonList:
            StateFlow<UIState<List<PokemonQuery.Pokemon_v2_pokemon>>>
        get() = _pokemonList

    private val _itemsList: MutableStateFlow<UIState<List<ItemsQuery.Pokemon_v2_item>>> =
        MutableStateFlow(UIState.LOADING)
    val itemsList:
            StateFlow<UIState<List<ItemsQuery.Pokemon_v2_item>>>
        get() = _itemsList

    protected fun getItemsList(){
        safeViewModelScope.launch {
            when(pokemonUseCases){
                is DexUseCases -> {
                    pokemonUseCases.getItemsList(appLanguage.value).collect{
                        _itemsList.value = it
                    }
                }
                is CreatePokemonUseCases -> {
                    pokemonUseCases.getItemsList(appLanguage.value).collect{
                        _itemsList.value = it
                    }
                }
            }

        }
    }

}