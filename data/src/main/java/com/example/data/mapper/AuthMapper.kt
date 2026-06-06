package com.example.data.mapper

import com.example.data.network.dto.response.AuthResponseDto
import com.example.domain.model.AuthResult

fun AuthResponseDto.toDomain(): AuthResult {
    return AuthResult(
        token = this.token,
        userId = this.userId,
        message = this.message
    )
}