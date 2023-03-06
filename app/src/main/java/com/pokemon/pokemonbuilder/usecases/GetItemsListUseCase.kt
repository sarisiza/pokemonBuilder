package com.pokemon.pokemonbuilder.usecases

import com.custom_libs_spil.network_connection.services.ServiceCall
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetItemsListUseCase(
    private val networkRepository: NetworkRepository,
    private val serviceCall: ServiceCall,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        language: LanguageEnum,
        version: Int? = null
    ): Flow<UIState<List<ItemsQuery.Pokemon_v2_item>>> = flow{
        emit(UIState.LOADING)
        serviceCall.serviceCallApi.apolloServiceCall(
            {networkRepository.getItems(language.language,version)},
            {emit(UIState.SUCCESS(it.pokemon_v2_item))},
            {emit(UIState.ERROR(it))}
        )
    }.flowOn(ioDispatcher)

}