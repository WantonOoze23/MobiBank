package com.example.domain.usecase.auth

import com.example.domain.model.AuthResult
import com.example.domain.model.RegisterData
import com.example.domain.repository.AuthRepository

class RegisterUseCase (private val authRepository: AuthRepository){
    suspend operator fun invoke(data: RegisterData) : Result<AuthResult> {
        if (
            data.firstName.isBlank() ||
            data.lastName.isBlank() ||
            data.email.isBlank() ||
            data.password.isBlank() ||
            data.phone.isBlank()
            ){
            return Result.failure(Exception("Fill in all fields"))
        }

        if ( data.password.length < 8 ) return Result.failure(Exception("Password must be at least 8 characters"))

        return authRepository.register(data)
    }
}