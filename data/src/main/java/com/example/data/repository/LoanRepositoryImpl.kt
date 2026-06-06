package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.network.api.LoanApi
import com.example.data.network.dto.request.ApplyLoanRequestDto
import com.example.domain.model.LoanModel
import com.example.domain.repository.LoanRepository

class LoanRepositoryImpl(
    private val loanApi: LoanApi
) : LoanRepository {
    override suspend fun applyForLoan(
        accountId: String,
        amount: Double
    ): Result<LoanModel> {
        return try {
            val request = ApplyLoanRequestDto(
                accountId = accountId,
                amount = amount
            )
            val response = loanApi.applyForLoan(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyLoans(): Result<List<LoanModel>> {
        return try {
            val response = loanApi.getMyLoans()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}