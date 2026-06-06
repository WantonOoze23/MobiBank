package com.example.data.mapper

import com.example.data.network.dto.response.DepositResponseDto
import com.example.domain.model.Currency
import com.example.domain.model.DepositModel
import com.example.domain.model.DepositStatus

fun DepositResponseDto.toDomain(): DepositModel {
    return DepositModel(
        id = this.id,
        amount = this.amount,
        interestRate = this.interestRate,
        currency = Currency.valueOf(this.currency),
        startDate = this.startDate,
        endDate = this.endDate,
        status = DepositStatus.valueOf(this.status)
    )
}