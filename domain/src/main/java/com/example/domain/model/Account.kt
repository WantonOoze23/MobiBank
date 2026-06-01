package com.example.domain.model

data class AccountModel(
    val id: String,
    val cardNumber: String,
    val currency: Currency,
    val balance: Double,
    val type: AccountType,
    val status: AccountStatus
)


enum class AccountType {
    DEBIT, CREDIT
}

enum class AccountStatus {
    ACTIVE, BLOCKED, CLOSED
}