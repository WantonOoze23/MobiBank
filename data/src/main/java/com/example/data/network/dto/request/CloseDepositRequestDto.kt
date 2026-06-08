package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CloseDepositRequestDto(
    val depositId: String,
    val accountId: String
)