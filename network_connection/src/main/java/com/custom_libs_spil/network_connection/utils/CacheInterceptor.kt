package com.custom_libs_spil.network_connection.utils

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val cacheControl =
            CacheControl.Builder()
                .maxAge(2,TimeUnit.MINUTES)
                .build()
        chain.request().newBuilder().apply {
            header("Cache-Control",cacheControl.toString())
        }.build().also { return chain.proceed(it) }
    }

}