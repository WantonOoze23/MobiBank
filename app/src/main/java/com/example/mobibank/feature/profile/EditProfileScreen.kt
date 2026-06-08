package com.example.mobibank.feature.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }

    // Заповнюємо поля поточними даними
    LaunchedEffect(profileState) {
        if (profileState is ProfileState.Success) {
            val profile = (profileState as ProfileState.Success).profile
            firstName = profile.firstName
            lastName = profile.lastName
            middleName = profile.middleName ?: ""
            email = profile.email
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is ProfileUpdateState.Success) {
            Toast.makeText(context, "Дані успішно оновлено!", Toast.LENGTH_SHORT).show()
            viewModel.resetUpdateState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Змінити дані") },
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
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it; viewModel.resetUpdateState() },
                label = { Text("Прізвище") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it; viewModel.resetUpdateState() },
                label = { Text("Ім'я") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = middleName,
                onValueChange = { middleName = it; viewModel.resetUpdateState() },
                label = { Text("По батькові") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; viewModel.resetUpdateState() },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it; viewModel.resetUpdateState() },
                label = { Text("Поточний пароль (для підтвердження)") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (updateState is ProfileUpdateState.Error) {
                Text(text = (updateState as ProfileUpdateState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.updateProfile(firstName, lastName, middleName.ifBlank { null }, email, currentPassword) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = updateState !is ProfileUpdateState.Loading
            ) {
                if (updateState is ProfileUpdateState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Зберегти зміни")
                }
            }
        }
    }
}