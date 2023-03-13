package com.pokemon.pokemonbuilder.usecases

import com.pokemon.pokemonbuilder.database.LocalRepository
import com.pokemon.pokemonbuilder.domain.PokemonTeam
import com.pokemon.pokemonbuilder.utils.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTeamsUseCase(
    private val localRepository: LocalRepository
) {
    operator fun invoke(): Flow<UIState<List<PokemonTeam>>> =
        flow{
            emit(UIState.LOADING)
            try {
                val pokemonTeams = localRepository.getAllTeams()
                emit(UIState.SUCCESS(pokemonTeams))
            } catch (e: Exception){
                emit(UIState.ERROR(e))
            }
        }
}