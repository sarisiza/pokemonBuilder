package com.pokemon.pokemonbuilder.utils

sealed class QueryType {
    object GENERATION: QueryType()
    object TYPE: QueryType()
    data class NAME(val search: Boolean): QueryType()
}