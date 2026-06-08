package com.example.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponseDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val phone: String,
    val email: String
)