package com.custom_libs_spil.network_connection.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkState(
    private val connectivityManager: ConnectivityManager
) {

    fun isInternetEnabled(): Boolean =
        connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false

}
