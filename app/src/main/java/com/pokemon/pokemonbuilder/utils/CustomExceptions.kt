package com.pokemon.pokemonbuilder.utils

class InternetConnectionException(message: String = "No internet connection"): Exception(message)

class NoResponseException(message: String = "Response is null"): Exception(message)