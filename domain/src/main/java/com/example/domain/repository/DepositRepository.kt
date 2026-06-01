package com.example.domain.repository

import com.example.domain.model.DepositModel

interface DepositRepository {

    // Передаємо ID картки з якої списуються кошти, суму та термін
    suspend fun openDeposit(
        accountId: String,
        amount: Double,
        durationMonths: Int
    ): Result<DepositModel>

    suspend fun getMyDeposits(): Result<List<DepositModel>>
}