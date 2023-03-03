package com.pokemon.pokemonbuilder.usecases

import com.custom_libs_spil.network_connection.services.ServiceCall
import com.pokemon.pokemonbuilder.MovesQuery
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMovesListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val serviceCall: ServiceCall,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        language: LanguageEnum,
        version: Int?
    ): Flow<UIState<List<MovesQuery.Pokemon_v2_move>>> = flow{
        emit(UIState.LOADING)
        serviceCall.serviceCallApi.apolloServiceCall(
            {networkRepository.getMoves(language.language,version)},
            {emit(UIState.SUCCESS(it.pokemon_v2_move))},
            {emit(UIState.ERROR(it))}
        )
    }.flowOn(ioDispatcher)

}