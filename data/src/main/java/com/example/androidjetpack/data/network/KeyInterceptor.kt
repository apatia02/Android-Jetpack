package com.example.androidjetpack.data.network

import com.example.androidjetpack.data.network.ServerConstants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Интерсептор, для добавления api ключа ко все запросам
 */
internal class KeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter(QUERY_PARAMETER_NAME, API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

    private companion object {
        const val QUERY_PARAMETER_NAME = "api_key"
    }
}