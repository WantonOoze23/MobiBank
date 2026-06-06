package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String
)