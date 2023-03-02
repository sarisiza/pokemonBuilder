package com.pokemon.pokemonbuilder.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.pokemon.pokemonbuilder.*
import com.pokemon.pokemonbuilder.utils.Language
import javax.inject.Inject

interface NetworkRepository {

    suspend fun getMoves(language: Language, version: Int?): ApolloResponse<MovesQuery.Data>

    suspend fun getItems(language: Language, version: Int?): ApolloResponse<ItemsQuery.Data>

    suspend fun getPokemon(language: Language, generation: Int): ApolloResponse<PokemonQuery.Data>

    suspend fun getTypes(language: Language): ApolloResponse<TypesQuery.Data>

    suspend fun getNatures(language: Language): ApolloResponse<NaturesQuery.Data>

}

class NetworkRepositoryImpl @Inject constructor(
    private val apolloService: ApolloClient
): NetworkRepository{

    override suspend fun getMoves(language: Language, version: Int?): ApolloResponse<MovesQuery.Data> {
        return version?.let {
            apolloService
                .query(MovesQuery(Optional.present(language.id), Optional.present(it)))
                .execute()
        }?: apolloService
            .query(MovesQuery(Optional.present(language.id), Optional.absent()))
            .execute()
    }


    override suspend fun getItems(language: Language, version: Int?): ApolloResponse<ItemsQuery.Data> {
        return version?.let {
            apolloService
                .query(ItemsQuery(Optional.present(language.id), Optional.present(it)))
                .execute()
        } ?: apolloService
            .query(ItemsQuery(Optional.present(language.id), Optional.absent()))
            .execute()
    }

    override suspend fun getPokemon(language: Language, generation: Int): ApolloResponse<PokemonQuery.Data> {
        return apolloService
            .query(PokemonQuery(Optional.present(language.id),Optional.present(generation)))
            .execute()
    }

    override suspend fun getTypes(language: Language): ApolloResponse<TypesQuery.Data> {
        return apolloService
            .query(TypesQuery(Optional.present(language.id)))
            .execute()
    }

    override suspend fun getNatures(language: Language): ApolloResponse<NaturesQuery.Data> {
        return apolloService
            .query(NaturesQuery(Optional.present(language.id)))
            .execute()
    }

}