package com.pokemon.pokemonbuilder.usecases

data class DexUseCases(
    val getPokemonList: GetPokemonListUseCase,
    val getMovesList: GetMovesListUseCase,
    val getItemsList: GetItemsListUseCase
)
