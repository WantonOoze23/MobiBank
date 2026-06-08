package com.example.domain.usecase.auth

import com.example.domain.model.UserProfileModel
import com.example.domain.repository.AuthRepository

class GetProfileUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<UserProfileModel> {
        return repository.getProfile()
    }
}