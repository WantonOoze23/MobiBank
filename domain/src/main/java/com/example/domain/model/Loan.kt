package com.example.domain.model

data class LoanModel(
    val id: String,
    val amount: Double,
    val remainingAmount: Double,
    val dueDate: String,
    val status: LoanStatus
)

enum class LoanStatus {
    ACTIVE, PAID, OVERDUE
}