package com.example.domain.model

data class TransactionModel(
    val id: String,
    val amount: Double,
    val currency: String,
    val type: TransactionType,
    val timestamp: String,
    val counterpartyCard: String? // Може бути null для внутрішніх операцій або зняття готівки
)

enum class TransactionType {
    EXPENSE, INCOME
}