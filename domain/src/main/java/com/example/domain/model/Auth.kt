package com.example.domain.model

data class AuthResult(
    val token: String,
    val userId: String?,
    val message: String?
)

// Дані, які користувач вводить при реєстрації
data class RegisterData(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val phone: String,
    val email: String,
    val password: String
)

// Дані для входу
data class LoginData(
    val login: String, // Телефон або email
    val password: String
)