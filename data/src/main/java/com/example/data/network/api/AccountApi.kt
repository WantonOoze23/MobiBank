package com.example.data.network.api

import com.example.data.network.dto.request.OpenCardRequestDto
import com.example.data.network.dto.response.AccountResponseDto
import retrofit2.http.*

interface AccountApi {
    @POST("/api/v1/accounts/open")
    suspend fun openCard(@Body request: OpenCardRequestDto): AccountResponseDto

    @GET("/api/v1/accounts")
    suspend fun getMyAccounts(): List<AccountResponseDto>
}