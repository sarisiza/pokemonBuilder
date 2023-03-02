package com.pokemon.pokemonbuilder.usecases

import com.custom_libs_spil.network_connection.utils.NetworkState
import com.pokemon.pokemonbuilder.MovesQuery
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMovesListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val networkState: NetworkState,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        language: LanguageEnum,
        version: Int?
    ): Flow<UIState<List<MovesQuery.Pokemon_v2_move>>> = flow{
        emit(UIState.LOADING)
        if(networkState.isInternetEnabled()){
            val response = networkRepository.getMoves(language.language,version).data
            response?.let {
                emit(UIState.SUCCESS(it.pokemon_v2_move))
            } ?: throw NoResponseException()
        }else throw InternetConnectionException()
    }.catch {
        emit(UIState.ERROR(it as Exception))
    }.flowOn(ioDispatcher)

}