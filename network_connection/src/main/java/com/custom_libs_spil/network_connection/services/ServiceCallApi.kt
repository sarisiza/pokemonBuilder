package com.custom_libs_spil.network_connection.services

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import retrofit2.Response

interface ServiceCallApi {

    suspend fun <D : Operation.Data>apolloServiceCall(
        action: suspend () -> ApolloResponse<D>,
        success: suspend (D) -> Unit,
        error: suspend (Exception) -> Unit
    )

    suspend fun <T>retrofitServiceCall(
        action: suspend () -> Response<T>,
        success: suspend (T) -> Unit,
        error: suspend (Exception) -> Unit
    )

}