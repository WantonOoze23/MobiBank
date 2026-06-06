package com.example.data.mapper

import com.example.data.network.dto.response.LoanResponseDto
import com.example.domain.model.LoanModel
import com.example.domain.model.LoanStatus

fun LoanResponseDto.toDomain(): LoanModel {
    return LoanModel(
        id = this.id,
        amount = this.amount,
        remainingAmount = this.remainingAmount,
        dueDate = this.dueDate,
        status = LoanStatus.valueOf(this.status)
    )
}