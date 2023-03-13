package com.pokemon.pokemonbuilder.usecases

import com.pokemon.pokemonbuilder.database.LocalRepository
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ModifyTeamNameUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        pokemonTeam: PokemonTeam,
        name: String
    ): Flow<UIState<Boolean>> = flow {
        emit(UIState.LOADING)
        try {
            pokemonTeam.name = name
            localRepository.modifyPokemonTeam(pokemonTeam)
            emit(UIState.SUCCESS(true))
        }catch (e: Exception){
            emit(UIState.ERROR(e))
        }
    }.flowOn(coroutineDispatcher)

}
