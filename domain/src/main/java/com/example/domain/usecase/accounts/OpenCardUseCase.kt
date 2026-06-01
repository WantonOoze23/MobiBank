package com.example.domain.usecase.accounts

import com.example.domain.model.AccountModel
import com.example.domain.model.AccountType
import com.example.domain.model.Currency
import com.example.domain.repository.AccountRepository

class OpenCardUseCase (private val repository: AccountRepository){
    suspend operator fun invoke(currency: Currency, type: AccountType = AccountType.DEBIT ) : Result<AccountModel> {
        return repository.openCard(currency = currency, type = type)
    }
}