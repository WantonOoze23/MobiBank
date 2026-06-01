package com.example.domain.repository

import com.example.domain.model.AccountModel
import com.example.domain.model.AccountType
import com.example.domain.model.Currency

interface AccountRepository {

    // Відкриття нової картки. Параметри беремо з серверного OpenCardRequest
    suspend fun openCard(
        currency: Currency,
        type: AccountType = AccountType.DEBIT
    ): Result<AccountModel>

    // Отримання списку всіх карток користувача
    suspend fun getMyAccounts(): Result<List<AccountModel>>
}