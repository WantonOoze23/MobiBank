package com.example.domain.usecase.accounts

import com.example.domain.model.AccountModel
import com.example.domain.repository.AccountRepository

class GetMyAccountsUseCase (private val repository: AccountRepository){
    suspend operator fun invoke() : Result<List<AccountModel>>{
        return repository.getMyAccounts()
    }
}