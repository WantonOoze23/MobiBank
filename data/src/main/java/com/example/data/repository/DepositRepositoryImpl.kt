package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.network.api.DepositApi
import com.example.data.network.dto.request.OpenDepositRequestDto
import com.example.domain.model.DepositModel
import com.example.domain.repository.DepositRepository

class DepositRepositoryImpl(
    private val depositApi: DepositApi
) : DepositRepository {
    override suspend fun openDeposit(
        accountId: String,
        amount: Double,
        durationMonths: Int
    ): Result<DepositModel> {
        return try {
            val request = OpenDepositRequestDto(
                accountId = accountId,
                amount = amount,
                durationMonths = durationMonths
            )
            val response = depositApi.openDeposit(request)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyDeposits(): Result<List<DepositModel>> {
        return try {
            val response = depositApi.getMyDeposits()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}