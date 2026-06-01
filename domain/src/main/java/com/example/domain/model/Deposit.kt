package com.example.domain.model

data class DepositModel(
    val id: String,
    val amount: Double,
    val interestRate: Double,
    val currency: Currency,
    val startDate: String,
    val endDate: String,
    val status: DepositStatus
)

enum class DepositStatus {
    ACTIVE, CLOSED
}