package com.example.domain.usecase.loans

import com.example.domain.model.LoanModel
import com.example.domain.repository.LoanRepository

class GetMyLoansUseCase(private val loanRepository: LoanRepository) {
    suspend operator fun invoke(): Result<List<LoanModel>> {
        return loanRepository.getMyLoans()
    }
}