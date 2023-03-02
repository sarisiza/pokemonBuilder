package com.pokemon.pokemonbuilder.utils

sealed class UIState<out T>{

    object LOADING: UIState<Nothing>()

    data class SUCCESS<T>(val response: T): UIState<T>()

    data class ERROR(val e: Exception): UIState<Nothing>()

}
