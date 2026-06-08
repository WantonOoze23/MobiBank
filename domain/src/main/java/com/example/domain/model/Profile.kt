package com.example.domain.model

data class UserProfileModel(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,
    val email: String
)