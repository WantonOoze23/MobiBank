package com.example.data.network.api

import com.example.data.network.dto.request.TransferRequestDto
import com.example.data.network.dto.response.TransactionResponseDto
import retrofit2.http.*

interface TransactionApi {
    @POST("/api/v1/transactions/transfer")
    suspend fun transfer(@Body request: TransferRequestDto): TransactionResponseDto

    @GET("/api/v1/transactions/{accountId}")
    suspend fun getHistory(
        @Path("accountId") accountId: String,
        @Query("type") filterType: String? = null
    ): List<TransactionResponseDto>
}