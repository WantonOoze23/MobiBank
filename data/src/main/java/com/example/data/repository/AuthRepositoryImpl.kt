package com.example.data.repository

import com.example.data.local.datastore.TokenManager
import com.example.data.mapper.toDomain
import com.example.data.network.api.AuthApi
import com.example.data.network.dto.request.ChangePasswordRequestDto
import com.example.data.network.dto.request.LoginRequestDto
import com.example.data.network.dto.request.RegisterRequestDto
import com.example.data.network.dto.request.UpdateProfileRequestDto
import com.example.domain.model.AuthResult
import com.example.domain.model.LoginData
import com.example.domain.model.RegisterData
import com.example.domain.model.UserProfileModel
import com.example.domain.repository.AuthRepository


class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun register(data: RegisterData): Result<AuthResult> {
        return try{
            val requestDto = RegisterRequestDto(
                firstName = data.firstName,
                lastName = data.lastName,
                middleName = data.middleName ?: "",
                phone = data.phone,
                email = data.email,
                password = data.password
            )

            val response = authApi.register(requestDto)

            tokenManager.saveSession(response.token, response.userId ?: "")
            Result.success(response.toDomain())
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun login(data: LoginData): Result<AuthResult> {
        return try{
            val requestDto = LoginRequestDto(
                login = data.login,
                password = data.password
            )
            val response = authApi.login(requestDto)

            tokenManager.saveSession(response.token, response.userId ?: "")
            Result.success(response.toDomain())

        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try{
            tokenManager.clearSession()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
    override suspend fun getProfile(): Result<UserProfileModel> {
        return try {
            val response = authApi.getProfile()
            val model = UserProfileModel(
                firstName = response.firstName,
                lastName = response.lastName,
                middleName = response.middleName,
                phone = response.phone,
                email = response.email
            )
            Result.success(model)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(firstName: String, lastName: String, middleName: String?, email: String, currentPassword: String): Result<String> {
        return try {
            val request = UpdateProfileRequestDto(firstName, lastName, middleName, email, currentPassword)
            val response = authApi.updateProfile(request)
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<String> {
        return try {
            val request = ChangePasswordRequestDto(oldPassword, newPassword)
            val response = authApi.changePassword(request)
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}