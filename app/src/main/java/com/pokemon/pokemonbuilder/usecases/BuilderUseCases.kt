package com.pokemon.pokemonbuilder.usecases

data class BuilderUseCases(
    val getTeams: GetTeamsUseCase,
    val getCreatedPokemon: GetCreatedPokemonUseCase,
    val createNewPokemon: CreateNewPokemonUseCase,
    val deletePokemon: DeletePokemonUseCase,
    val modifyPokemon: ModifyPokemonUseCase,
    val addPokemonToTeam: AddPokemonToTeamUseCase,
    val removePokemonFromTeam: RemovePokemonFromTeamUseCase,
    val getPokemonInTeam: GetPokemonInTeamUseCase,
    val modifyTeamName: ModifyTeamNameUseCase
)
