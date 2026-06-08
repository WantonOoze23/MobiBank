package com.example.mobibank.feature.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val passwordState by viewModel.passwordState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(passwordState) {
        if (passwordState is ChangePasswordState.Success) {
            Toast.makeText(context, "Пароль успішно змінено!", Toast.LENGTH_SHORT).show()
            viewModel.resetPasswordState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Змінити пароль") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it; viewModel.resetPasswordState() },
                label = { Text("Старий пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it; viewModel.resetPasswordState() },
                label = { Text("Новий пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; viewModel.resetPasswordState() },
                label = { Text("Підтвердіть новий пароль") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (passwordState is ChangePasswordState.Error) {
                Text(text = (passwordState as ChangePasswordState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (newPassword != confirmPassword) {
                        Toast.makeText(context, "Нові паролі не співпадають", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.changePassword(oldPassword, newPassword)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = passwordState !is ChangePasswordState.Loading && oldPassword.isNotBlank() && newPassword.isNotBlank()
            ) {
                if (passwordState is ChangePasswordState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Змінити пароль")
                }
            }
        }
    }
}