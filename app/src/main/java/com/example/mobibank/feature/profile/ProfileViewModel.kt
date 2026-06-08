package com.example.mobibank.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.datastore.TokenManager
import com.example.domain.usecase.auth.GetProfileUseCase
import com.example.domain.usecase.auth.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val tokenManager: TokenManager,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<ProfileUpdateState>(ProfileUpdateState.Idle)
    val updateState: StateFlow<ProfileUpdateState> = _updateState.asStateFlow()

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

    fun updateProfile(firstName: String, lastName: String, middleName: String?, email: String) {
        viewModelScope.launch {
            _updateState.value = ProfileUpdateState.Loading
            val result = updateProfileUseCase(firstName, lastName, middleName, email)

            result.onSuccess {
                _updateState.value = ProfileUpdateState.Success
                loadProfile() // Оновлюємо дані після успішного збереження
            }.onFailure { exception ->
                _updateState.value = ProfileUpdateState.Error(exception.message ?: "Помилка оновлення")
            }
        }
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