package com.example.mobibank.feature.profile

import com.example.domain.model.UserProfileModel

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: UserProfileModel) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

sealed class ProfileUpdateState {
    object Idle : ProfileUpdateState()
    object Loading : ProfileUpdateState()
    object Success : ProfileUpdateState()
    data class Error(val message: String) : ProfileUpdateState()
}

sealed class ChangePasswordState {
    object Idle : ChangePasswordState()
    object Loading : ChangePasswordState()
    object Success : ChangePasswordState()
    data class Error(val message: String) : ChangePasswordState()
}