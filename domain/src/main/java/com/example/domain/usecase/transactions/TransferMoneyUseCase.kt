package com.example.domain.usecase.transactions

import com.example.domain.model.TransactionModel
import com.example.domain.repository.TransactionRepository

class TransferMoneyUseCase (private val repository: TransactionRepository) {
    suspend operator fun invoke(
        userId : String,
        receiverCardNumber : String,
        amount : Double,
    ) : Result<TransactionModel> {
        if (amount <= 0)
            return Result.failure(IllegalArgumentException("Amount must be greater than zero"))
        if (receiverCardNumber.length != 16)
            return Result.failure(IllegalArgumentException("Receiver card number must be 16 digits"))

        return repository.transfer(userId, receiverCardNumber, amount)
    }
}