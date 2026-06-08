package com.example.domain.repository

import com.example.domain.model.LoanModel

interface LoanRepository {

    // Оформлення кредиту на певну картку
    suspend fun applyForLoan(
        accountId: String,
        amount: Double,
        termInDays: Int
    ): Result<LoanModel>

    suspend fun repayLoan(
        loanId: String,
        accountId: String
    ): Result<String>

    suspend fun getMyLoans(): Result<List<LoanModel>>
}