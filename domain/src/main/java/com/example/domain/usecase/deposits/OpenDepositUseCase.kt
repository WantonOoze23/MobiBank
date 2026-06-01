package com.example.domain.usecase.deposits

import com.example.domain.model.DepositModel
import com.example.domain.repository.DepositRepository

class OpenDepositUseCase(private val repository: DepositRepository){
    suspend operator fun invoke(
        userId: String,
        amount: Double,
        durationMonths: Int
    ) : Result<DepositModel>{
        if(amount <= 0) return Result.failure(Exception("Amount must be greater than 0"))
        if(durationMonths <= 1) return Result.failure(Exception("Duration must be greater than 1"))

        return repository.openDeposit(userId, amount, durationMonths)
    }
}
