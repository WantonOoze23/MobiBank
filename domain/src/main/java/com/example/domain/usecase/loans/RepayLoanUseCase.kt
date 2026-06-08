package com.example.domain.usecase.loans

import com.example.domain.repository.LoanRepository

class RepayLoanUseCase(private val repository: LoanRepository) {
    suspend operator fun invoke(loanId: String, accountId: String): Result<String> {
        return repository.repayLoan(loanId, accountId)
    }
}