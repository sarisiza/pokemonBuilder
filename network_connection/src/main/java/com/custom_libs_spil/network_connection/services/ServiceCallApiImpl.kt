package com.custom_libs_spil.network_connection.services

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.custom_libs_spil.network_connection.utils.InternetConnectionException
import com.custom_libs_spil.network_connection.utils.NetworkState
import com.custom_libs_spil.network_connection.utils.NoResponseException
import com.custom_libs_spil.network_connection.utils.ResponseFailedException
import retrofit2.Response

class ServiceCallApiImpl(
    private val networkState: NetworkState
): ServiceCallApi {

    override suspend fun <D : Operation.Data> apolloServiceCall(
        action: suspend () -> ApolloResponse<D>,
        success: suspend (D) -> Unit,
        error: suspend (Exception) -> Unit
    ) {
        try {
            if (networkState.isInternetEnabled()) {
                val response = action.invoke()
                response.data?.let {
                    success(it)
                } ?: throw NoResponseException()
            } else throw InternetConnectionException()
        } catch (e: Exception){
            error(e)
        }
    }

    override suspend fun <T> retrofitServiceCall(
        action: suspend () -> Response<T>,
        success: suspend (T) -> Unit,
        error: suspend (Exception) -> Unit
    ) {
        try{
            if(networkState.isInternetEnabled()) {
                val response = action.invoke()
                if (response.isSuccessful) {
                    response.body()?.let {
                        success(it)
                    } ?: throw NoResponseException()
                } else throw ResponseFailedException()
            }else throw InternetConnectionException()
        } catch (e: Exception){
            error(e)
        }
    }

}