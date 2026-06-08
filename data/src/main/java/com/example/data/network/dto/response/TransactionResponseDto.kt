package com.example.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponseDto(
    val id: String,
    val amount: Double,
    val currency: String,
    val type: String,
    val timestamp: String,
    val counterpartyCard: String?
)