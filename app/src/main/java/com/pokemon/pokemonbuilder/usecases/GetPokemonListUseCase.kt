package com.pokemon.pokemonbuilder.usecases

import com.custom_libs_spil.network_connection.utils.NetworkState
import com.pokemon.pokemonbuilder.PokemonQuery
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val networkState: NetworkState,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        language: LanguageEnum,
        generation: Int
    ): Flow<UIState<List<PokemonQuery.Pokemon_v2_pokemon>>> = flow{
        emit(UIState.LOADING)
        if(networkState.isInternetEnabled()){
            val response = networkRepository.getPokemon(language.language,generation)
            if(!response.hasErrors()){
                response.data?.let {
                    emit(UIState.SUCCESS(it.pokemon_v2_pokemon))
                }?: throw NoResponseException()
            }
        } else throw InternetConnectionException()
    }.catch {
        emit(UIState.ERROR(it as Exception))
    }.flowOn(ioDispatcher)

}