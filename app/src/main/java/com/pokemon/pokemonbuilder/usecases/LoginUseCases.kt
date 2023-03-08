package com.pokemon.pokemonbuilder.usecases

data class LoginUseCases(
    val signUpUseCase: SignUpUseCase,
    val languageUseCase: PickLanguageUseCase,
    val getDatastoreUseCase: GetDatastoreUseCase,
)