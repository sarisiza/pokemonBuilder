package com.pokemon.pokemonbuilder.usecases

data class LoginUseCases(
    val signUpUseCase: SignUpUseCase,
    val languageUseCase: PickLanguageUseCase,
    val getLoginInfoUseCase: GetLoginInfoUseCase,
    val getLanguageUseCase: GetLanguageUseCase,
    val checkIfFirstTimeUseCase: CheckIfFirstTimeUseCase
)