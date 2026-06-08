package com.example.domain.usecase.auth

import com.example.domain.repository.AuthRepository

class UpdateProfileUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(firstName: String, lastName: String, middleName: String?, email: String, currentPassword: String): Result<String> {
        return repository.updateProfile(firstName, lastName, middleName, email, currentPassword)
    }
}