package com.example.data.network.interceptor

import com.example.data.local.datastore.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.getToken.first() }

        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            runBlocking {
                tokenManager.clearSession()
            }
        }

        return response
    }
}