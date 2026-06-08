package com.example.data.network.api

import com.example.data.network.dto.request.LoginRequestDto
import com.example.data.network.dto.request.RegisterRequestDto
import com.example.data.network.dto.request.UpdateProfileRequestDto
import com.example.data.network.dto.response.AuthResponseDto
import com.example.data.network.dto.response.MessageResponseDto
import com.example.data.network.dto.response.UserProfileResponseDto
import retrofit2.http.*

interface AuthApi {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @GET("/api/v1/profile")
    suspend fun getProfile(): UserProfileResponseDto

    @PUT("/api/v1/profile/update")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): MessageResponseDto
}