package com.example.mobibank.feature.deposits

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

data class DepositTermOption(val label: String, val months: Int)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenDepositScreen(
    accountId: String,
    onNavigateBack: () -> Unit,
    viewModel: DepositsViewModel = koinViewModel()
) {
    val openState by viewModel.openState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }

    val termOptions = listOf(
        DepositTermOption("3 місяці (8%)", 3),
        DepositTermOption("6 місяців (11%)", 6),
        DepositTermOption("12 місяців (14%)", 12)
    )
    var selectedTerm by remember { mutableStateOf(termOptions[0]) }

    LaunchedEffect(openState) {
        if (openState is OpenDepositState.Success) {
            Toast.makeText(context, "Депозит успішно відкрито!", Toast.LENGTH_SHORT).show()
            viewModel.resetOpenDepositState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Відкрити депозит") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp),
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Сума депозиту") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Оберіть термін:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                termOptions.forEach { option ->
                    FilterChip(
                        selected = selectedTerm == option,
                        onClick = { selectedTerm = option },
                        label = { Text(option.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (openState is OpenDepositState.Error) {
                Text(text = (openState as OpenDepositState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    val amount = amount.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.openDeposit(accountId, amount, selectedTerm.months)
                    } else {
                        Toast.makeText(context, "Введіть коректну суму", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = openState !is OpenDepositState.Loading && amount.isNotBlank()
            ) {
                if (openState is OpenDepositState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Підтвердити відкриття")
                }
            }
        }
    }
}