package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class ChangePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(oldPassword: String, newPassword: String): Result<String> {
        return repository.changePassword(oldPassword, newPassword)
    }
}