package com.example.domain.usecase.deposits

import com.example.domain.model.DepositModel
import com.example.domain.repository.DepositRepository

class GetMyDepositsUseCase (private val repository: DepositRepository){
    suspend operator fun invoke(): Result<List<DepositModel>> {
        return repository.getMyDeposits()
    }
}