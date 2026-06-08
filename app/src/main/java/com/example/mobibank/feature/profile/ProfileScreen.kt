package com.example.mobibank.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Особистий кабінет") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (profileState) {
                is ProfileState.Loading -> CircularProgressIndicator()
                is ProfileState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = (profileState as ProfileState.Error).message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProfile() }) { Text("Спробувати ще раз") }
                    }
                }
                is ProfileState.Success -> {
                    val profile = (profileState as ProfileState.Success).profile

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "${profile.lastName} ${profile.firstName} ${profile.middleName ?: ""}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = profile.email, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = profile.phone, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = onNavigateToEdit,
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Змінити дані")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onNavigateToChangePassword,
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Змінити пароль")
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        OutlinedButton(
                            onClick = { viewModel.logout() },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Вийти з акаунту")
                        }
                    }
                }
            }
        }
    }
}