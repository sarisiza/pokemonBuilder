package com.pokemon.pokemonbuilder.usecases

data class DexUseCases(
    val getPokemonList: GetPokemonListUseCase,
    val getItemsList: GetItemsListUseCase,
    val getNaturesList: GetNaturesListUseCase,
    val getTypesList: GetTypesListUseCase
)
