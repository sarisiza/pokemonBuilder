package com.custom_libs_spil.network_connection.utils

class InternetConnectionException(message: String = "No internet connection"): Exception(message)

class NoResponseException(message: String = "Response is null"): Exception(message)

class ResponseFailedException(message: String = "Failed to retrieve response"): Exception(message)