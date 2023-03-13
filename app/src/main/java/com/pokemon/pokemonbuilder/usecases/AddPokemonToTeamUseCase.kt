package com.pokemon.pokemonbuilder.usecases

import com.pokemon.pokemonbuilder.database.LocalRepository
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.TeamIsFull
import com.pokemon.pokemonbuilder.utils.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AddPokemonToTeamUseCase(
    private val localRepository: LocalRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        pokemonTeam: PokemonTeam,
        pokemon: Pokemon
    ): Flow<UIState<Boolean>> = flow {
        emit(UIState.LOADING)
        try {
            if(!pokemonTeam.isComplete()){
                pokemonTeam.pokemon.add(pokemon.id)
                localRepository.modifyPokemonTeam(pokemonTeam)
                emit(UIState.SUCCESS(true))
            } else throw TeamIsFull("${pokemonTeam.name} team is full")
        } catch (e: Exception){
            emit(UIState.ERROR(e))
        }
    }.flowOn(coroutineDispatcher)

}
