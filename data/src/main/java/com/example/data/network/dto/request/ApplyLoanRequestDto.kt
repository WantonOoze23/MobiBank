package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ApplyLoanRequestDto(
    val accountId: String,
    val amount: Double
)