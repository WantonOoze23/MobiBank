package com.example.mobibank.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LoginData
import com.example.domain.model.RegisterData
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(data: LoginData){
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = loginUseCase(data)

            result.onSuccess {
                _authState.value = AuthState.Success
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun register(data: RegisterData){
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = registerUseCase(data)

            result.onSuccess {
                _authState.value = AuthState.Success
            }.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun resetState(){
        _authState.value = AuthState.Idle
    }
}