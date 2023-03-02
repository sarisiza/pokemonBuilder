package com.custom_libs_spil.network_connection.utils

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class ForceCacheInterceptor(
    private val networkState: NetworkState
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.request().newBuilder().apply {
            if(!networkState.isInternetEnabled()){
                cacheControl(CacheControl.FORCE_CACHE)
            }
        }.build().also { return chain.proceed(it) }
    }

}