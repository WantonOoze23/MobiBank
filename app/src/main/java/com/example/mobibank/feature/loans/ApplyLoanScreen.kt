package com.example.mobibank.feature.loans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLoanScreen(
    accountId: String, // ID картки, куди зарахуються гроші
    onNavigateBack: () -> Unit,
    viewModel: LoansViewModel = koinViewModel()
) {
    val applyState by viewModel.applyState.collectAsStateWithLifecycle()
    var amount by remember { mutableStateOf("") }

    LaunchedEffect(applyState) {
        if (applyState is ApplyLoanState.Success) {
            viewModel.resetApplyState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Оформити позику") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ставка: 5% | Термін: 30 днів",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it; viewModel.resetApplyState() },
                label = { Text("Бажана сума") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            // Динамічний підрахунок того, скільки доведеться повернути
            val amountValue = amount.toDoubleOrNull() ?: 0.0
            val totalToRepay = amountValue + (amountValue * 0.05)

            if (amountValue > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "До повернення: $totalToRepay ₴",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (applyState is ApplyLoanState.Error) {
                Text(text = (applyState as ApplyLoanState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (applyState is ApplyLoanState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        amount.toDoubleOrNull()?.let {
                            viewModel.applyForLoan(accountId, it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = amount.isNotEmpty() && amountValue > 0
                ) {
                    Text("Отримати кошти")
                }
            }
        }
    }
}