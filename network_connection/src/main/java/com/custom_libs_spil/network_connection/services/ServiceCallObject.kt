package com.custom_libs_spil.network_connection.services

import com.custom_libs_spil.network_connection.utils.NetworkState

class ServiceCall(
    private val networkState: NetworkState
) {

    val serviceCallApi: ServiceCallApi by lazy {
        ServiceCallApiImpl(networkState)
    }

}