package com.pokemon.pokemonbuilder.viewmodel

import com.pokemon.pokemonbuilder.MovesQuery
import com.pokemon.pokemonbuilder.usecases.DexUseCases
import com.pokemon.pokemonbuilder.usecases.PokemonUseCasesParent
import com.pokemon.pokemonbuilder.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DexViewModel @Inject constructor(
    private val dexUseCases: DexUseCases
): PokemonViewModel(dexUseCases) {

    private val _movesList: MutableStateFlow<UIState<List<MovesQuery.Pokemon_v2_move>>> =
        MutableStateFlow(UIState.LOADING)
    val movesList:
            StateFlow<UIState<List<MovesQuery.Pokemon_v2_move>>> get() = _movesList

    fun getIntent(intent: ViewIntents){
        when(intent){
            is ViewIntents.GET_POKEMON -> {
                getPokemonList(intent.generation)
            }
            ViewIntents.GET_ITEMS -> {
                getItemsList()
            }
            ViewIntents.GET_MOVES -> {
                getMovesList()
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

    private fun getMovesList(){
        safeViewModelScope.launch {
            dexUseCases.getMovesList(appLanguage.value).collect{
                _movesList.value = it
            }
        }
    }

}