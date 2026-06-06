package com.example.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DepositResponseDto(
    val id: String,
    val amount: Double,
    val interestRate: Double,
    val currency: String,
    val startDate: String,
    val endDate: String,
    val status: String
)
