package com.example.domain.usecase.auth

import com.example.domain.model.AuthResult
import com.example.domain.model.LoginData
import com.example.domain.repository.AuthRepository

class LoginUseCase (private val authRepository: AuthRepository){
    suspend operator fun invoke(data: LoginData) : Result<AuthResult>{
        if(data.login.isEmpty() || data.password.isEmpty()) return Result.failure(Exception("Fill in all fields"))
        return authRepository.login(data)
    }
}