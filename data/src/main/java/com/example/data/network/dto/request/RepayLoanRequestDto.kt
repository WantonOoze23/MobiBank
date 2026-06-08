package com.example.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RepayLoanRequestDto(
    val loanId: String,
    val accountId: String
)