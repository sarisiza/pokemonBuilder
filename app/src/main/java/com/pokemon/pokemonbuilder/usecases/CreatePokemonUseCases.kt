package com.pokemon.pokemonbuilder.usecases

data class CreatePokemonUseCases(
    val getItemsList: GetItemsListUseCase
): PokemonUseCasesParent()
