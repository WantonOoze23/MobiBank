package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class TransferRequestDto(
    val senderAccountId: String,
    val receiverCardNumber: String,
    val amount: Double
)