package com.example.domain.repository

import com.example.domain.model.LoanModel

interface LoanRepository {

    // Оформлення кредиту на певну картку
    suspend fun applyForLoan(
        accountId: String,
        amount: Double
    ): Result<LoanModel>

    suspend fun getMyLoans(): Result<List<LoanModel>>
}