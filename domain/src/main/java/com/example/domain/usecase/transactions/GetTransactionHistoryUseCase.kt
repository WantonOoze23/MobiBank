package com.example.domain.usecase.transactions

import com.example.domain.model.TransactionModel
import com.example.domain.model.TransactionType
import com.example.domain.repository.TransactionRepository

class GetTransactionHistoryUseCase (private val repository: TransactionRepository){
    suspend operator fun invoke(userId : String, transactionFilter : TransactionType? = null) : Result<List<TransactionModel>>{
        return repository.getHistory(userId, transactionFilter)
    }
}