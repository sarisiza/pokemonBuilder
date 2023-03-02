package com.pokemon.pokemonbuilder.di

import android.content.Context
import android.net.ConnectivityManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.custom_libs_spil.network_connection.utils.CacheInterceptor
import com.custom_libs_spil.network_connection.utils.ForceCacheInterceptor
import com.custom_libs_spil.network_connection.utils.NetworkState
import com.pokemon.pokemonbuilder.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesApolloClient(okHttpClient: OkHttpClient) =
        ApolloClient.Builder()
            .okHttpClient(okHttpClient)
            .serverUrl(BASE_URL)
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient(
        cache: Cache,
        cacheInterceptor: CacheInterceptor,
        forceCacheInterceptor: ForceCacheInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(cacheInterceptor)
            .cache(cache)
            .addInterceptor(forceCacheInterceptor)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
            .connectTimeout(30,TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun providesForceCacheInterceptor(
        networkState: NetworkState
    ) = ForceCacheInterceptor(networkState)

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun providesNetworkState(
        connectivityManager: ConnectivityManager
    ) = NetworkState(connectivityManager)

    @Provides
    @Singleton
    fun providesCacheInterceptor() = CacheInterceptor()

    @Provides
    @Singleton
    fun providesCacheFile(
        @ApplicationContext context: Context
    ) = Cache(File(context.cacheDir,"http-cache"),10L*1024L*1024L)

}