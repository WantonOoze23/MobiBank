package com.example.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val token: String,
    val userId: String? = null,
    val message: String? = null
)