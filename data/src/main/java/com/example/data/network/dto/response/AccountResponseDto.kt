package com.example.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponseDto(
    val id: String,
    val cardNumber: String,
    val currency: String,
    val balance: Double,
    val type: String,
    val status: String
)