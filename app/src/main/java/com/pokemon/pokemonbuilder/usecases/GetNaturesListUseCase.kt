package com.pokemon.pokemonbuilder.usecases

import com.custom_libs_spil.network_connection.services.ServiceCall
import com.pokemon.pokemonbuilder.NaturesQuery
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.utils.LanguageEnum
import com.pokemon.pokemonbuilder.utils.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNaturesListUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val serviceCall: ServiceCall,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        language: LanguageEnum
    ): Flow<UIState<List<NaturesQuery.Pokemon_v2_nature>>> = flow {
        emit(UIState.LOADING)
        serviceCall.serviceCallApi.apolloServiceCall(
            {networkRepository.getNatures(language.language)},
            {emit(UIState.SUCCESS(it.pokemon_v2_nature))},
            {emit(UIState.ERROR(it))}
        )
    }.flowOn(ioDispatcher)

}