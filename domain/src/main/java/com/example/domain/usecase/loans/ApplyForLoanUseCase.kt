package com.example.domain.usecase.loans

import com.example.domain.model.LoanModel
import com.example.domain.repository.LoanRepository

class ApplyForLoanUseCase (private val repository: LoanRepository){
    suspend operator fun invoke(userId: String, amount: Double) : Result<LoanModel>{
        if(amount <= 0) return Result.failure(Exception("Amount must be greater than 0"))

        return repository.applyForLoan(userId, amount)
    }
}