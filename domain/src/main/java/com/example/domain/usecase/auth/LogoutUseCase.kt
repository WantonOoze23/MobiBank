package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() : Result<Unit> {
        return repository.logout()
    }
}