package com.example.mobibank.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.datastore.TokenManager
import com.example.domain.usecase.auth.ChangePasswordUseCase
import com.example.domain.usecase.auth.GetProfileUseCase
import com.example.domain.usecase.auth.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val tokenManager: TokenManager,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<ProfileUpdateState>(ProfileUpdateState.Idle)
    val updateState: StateFlow<ProfileUpdateState> = _updateState.asStateFlow()

    private val _passwordState = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val passwordState: StateFlow<ChangePasswordState> = _passwordState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            val result = getProfileUseCase()
            result.onSuccess { profile ->
                _profileState.value = ProfileState.Success(profile)
            }.onFailure { exception ->
                _profileState.value = ProfileState.Error(exception.message ?: "Помилка завантаження")
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, middleName: String?, email: String, currentPassword: String) {
        viewModelScope.launch {
            _updateState.value = ProfileUpdateState.Loading
            val result = updateProfileUseCase(firstName, lastName, middleName, email, currentPassword)

            result.onSuccess {
                _updateState.value = ProfileUpdateState.Success
                loadProfile()
            }.onFailure { exception ->
                _updateState.value = ProfileUpdateState.Error(exception.message ?: "Помилка оновлення")
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _passwordState.value = ChangePasswordState.Loading

            val result = changePasswordUseCase(oldPassword, newPassword)

            result.onSuccess {
                _passwordState.value = ChangePasswordState.Success
            }.onFailure { exception ->
                _passwordState.value = ChangePasswordState.Error(exception.message ?: "Помилка зміни пароля")
            }
        }
    }

    fun resetPasswordState() {
        _passwordState.value = ChangePasswordState.Idle
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearSession()
        }
    }

    fun resetUpdateState() {
        _updateState.value = ProfileUpdateState.Idle
    }
}