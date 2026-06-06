package com.example.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class LoanResponseDto(
    val id: String,
    val amount: Double,
    val remainingAmount: Double,
    val dueDate: String,
    val status: String
)