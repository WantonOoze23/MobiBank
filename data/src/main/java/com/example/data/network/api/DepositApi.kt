package com.example.data.network.api

import com.example.data.network.dto.request.OpenDepositRequestDto
import com.example.data.network.dto.response.DepositResponseDto
import retrofit2.http.*

interface DepositApi {
    @POST("/api/v1/deposits/open")
    suspend fun openDeposit(@Body request: OpenDepositRequestDto): DepositResponseDto

    @GET("/api/v1/deposits")
    suspend fun getMyDeposits(): List<DepositResponseDto>
}