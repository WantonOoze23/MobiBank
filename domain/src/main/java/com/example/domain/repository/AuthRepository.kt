package com.example.domain.repository

import com.example.domain.model.AuthResult
import com.example.domain.model.LoginData
import com.example.domain.model.RegisterData
import com.example.domain.model.UserProfileModel

interface AuthRepository {

    suspend fun register(data: RegisterData): Result<AuthResult>

    suspend fun login(data: LoginData): Result<AuthResult>

    suspend fun logout(): Result<Unit>
    suspend fun getProfile(): Result<UserProfileModel>
    suspend fun updateProfile(firstName: String, lastName: String, middleName: String?, email: String, currentPassword: String): Result<String>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<String>
}