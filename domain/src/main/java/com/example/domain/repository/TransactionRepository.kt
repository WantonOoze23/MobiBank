package com.example.domain.repository

import com.example.domain.model.TransactionModel
import com.example.domain.model.TransactionType

interface TransactionRepository {

    // Переказ коштів. Відправляємо ID нашої картки та номер картки отримувача
    suspend fun transfer(
        senderAccountId: String,
        receiverCardNumber: String,
        amount: Double
    ): Result<TransactionModel>

    // Отримання історії. filterType робимо nullable, щоб мати змогу отримати всі транзакції
    suspend fun getHistory(
        accountId: String,
        filterType: TransactionType? = null
    ): Result<List<TransactionModel>>
}