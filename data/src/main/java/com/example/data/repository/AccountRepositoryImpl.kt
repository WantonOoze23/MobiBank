package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.network.api.AccountApi
import com.example.data.network.dto.request.OpenCardRequestDto
import com.example.domain.model.AccountModel
import com.example.domain.model.AccountType
import com.example.domain.model.Currency
import com.example.domain.repository.AccountRepository

class AccountRepositoryImpl(
    private val accountApi: AccountApi
) : AccountRepository {
    override suspend fun openCard(
        currency: Currency,
        type: AccountType
    ): Result<AccountModel> {
        return try {
            val response = accountApi.openCard(OpenCardRequestDto(currency.name, type.name))
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyAccounts(): Result<List<AccountModel>> {
        return try {
            val response = accountApi.getMyAccounts()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}