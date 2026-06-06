package com.example.data.mapper

import com.example.data.network.dto.response.TransactionResponseDto
import com.example.domain.model.TransactionModel
import com.example.domain.model.TransactionType

fun TransactionResponseDto.toDomain(): TransactionModel {
    return TransactionModel(
        id = this.id,
        amount = this.amount,
        type = TransactionType.valueOf(this.type),
        timestamp = this.timestamp,
        counterpartyCard = this.counterpartyCard
    )
}