package com.example.domain.usecase.deposits

import com.example.domain.repository.DepositRepository

class CloseDepositUseCase(private val repository: DepositRepository) {
    suspend operator fun invoke(depositId: String, accountId: String): Result<String> {
        return repository.closeDeposit(depositId, accountId)
    }
}