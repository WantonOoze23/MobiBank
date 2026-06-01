package com.example.domain.repository

import com.example.domain.model.AuthResult
import com.example.domain.model.LoginData
import com.example.domain.model.RegisterData

interface AuthRepository {

    suspend fun register(data: RegisterData): Result<AuthResult>

    suspend fun login(data: LoginData): Result<AuthResult>

    suspend fun logout(): Result<Unit>
}