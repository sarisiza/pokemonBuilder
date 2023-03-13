package com.pokemon.pokemonbuilder.usecases

import com.pokemon.pokemonbuilder.database.LocalRepository
import com.pokemon.pokemonbuilder.domain.Pokemon
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPokemonInTeamUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        pokemonTeam: PokemonTeam
    ): Flow<UIState<List<Pokemon>>> = flow {
        emit(UIState.LOADING)
        try {
            val pokemonIdList = pokemonTeam.pokemon
            val pokemonList: MutableList<Pokemon> = mutableListOf()
            pokemonIdList.forEach {
                pokemonList.add(localRepository.getPokemonFromId(it))
            }
            emit(UIState.SUCCESS(pokemonList))
        }catch (e: Exception){
            emit(UIState.ERROR(e))
        }
    }.flowOn(coroutineDispatcher)

}
