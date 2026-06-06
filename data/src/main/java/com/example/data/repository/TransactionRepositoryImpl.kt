package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.network.api.TransactionApi
import com.example.data.network.dto.request.TransferRequestDto
import com.example.domain.model.TransactionModel
import com.example.domain.model.TransactionType
import com.example.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionApi: TransactionApi
) : TransactionRepository {
    override suspend fun transfer(
        senderAccountId: String,
        receiverCardNumber: String,
        amount: Double
    ): Result<TransactionModel> {
        return try {
            val request = TransferRequestDto(
                senderAccountId,
                receiverCardNumber,
                amount
            )

            val response = transactionApi.transfer(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistory(
        accountId: String,
        filterType: TransactionType?
    ): Result<List<TransactionModel>> {
        return try {
            val response = transactionApi.getHistory(
                accountId,
                filterType?.name
            )
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}