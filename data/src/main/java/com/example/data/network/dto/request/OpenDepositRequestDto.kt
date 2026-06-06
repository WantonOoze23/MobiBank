package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class OpenDepositRequestDto(
    val accountId: String,
    val amount: Double,
    val durationMonths: Int
)