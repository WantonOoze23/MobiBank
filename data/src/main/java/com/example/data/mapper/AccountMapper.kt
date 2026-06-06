package com.example.data.mapper

import com.example.data.network.dto.response.AccountResponseDto
import com.example.domain.model.AccountModel
import com.example.domain.model.AccountStatus
import com.example.domain.model.AccountType
import com.example.domain.model.Currency

fun AccountResponseDto.toDomain(): AccountModel {
    return AccountModel(
        id = this.id,
        cardNumber = this.cardNumber,
        currency = Currency.valueOf(this.currency),
        balance = this.balance,
        type = AccountType.valueOf(this.type),
        status = AccountStatus.valueOf(this.status)
    )
}