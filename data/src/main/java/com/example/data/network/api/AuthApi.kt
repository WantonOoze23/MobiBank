package com.example.data.network.api

import com.example.data.network.dto.request.LoginRequestDto
import com.example.data.network.dto.request.RegisterRequestDto
import com.example.data.network.dto.response.AuthResponseDto
import retrofit2.http.*

interface AuthApi {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto
}