package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val email: String,
    val currentPassword: String
)