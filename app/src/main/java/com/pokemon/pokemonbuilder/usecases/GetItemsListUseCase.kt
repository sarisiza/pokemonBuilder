package com.pokemon.pokemonbuilder.usecases

import com.custom_libs_spil.network_connection.utils.NetworkState
import com.pokemon.pokemonbuilder.ItemsQuery
import com.pokemon.pokemonbuilder.service.NetworkRepository
import com.pokemon.pokemonbuilder.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetItemsListUseCase(
    private val networkRepository: NetworkRepository,
    private val networkState: NetworkState,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        language: LanguageEnum,
        version: Int?
    ): Flow<UIState<List<ItemsQuery.Pokemon_v2_item>>> = flow{
        emit(UIState.LOADING)
        if(networkState.isInternetEnabled()){
            val response = networkRepository.getItems(language.language,version).data
            response?.let {
                emit(UIState.SUCCESS(it.pokemon_v2_item))
            }?: throw NoResponseException()
        }else throw InternetConnectionException()
    }.catch {
        emit(UIState.ERROR(it as Exception))
    }.flowOn(ioDispatcher)

}