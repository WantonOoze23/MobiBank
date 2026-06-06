package com.example.mobibank.feature.deposits

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
fun OpenDepositScreen(
    accountId: String,
    onNavigateBack: () -> Unit,
    viewModel: DepositsViewModel = koinViewModel()
) {
    val openState by viewModel.openState.collectAsStateWithLifecycle()

    var amount by remember { mutableStateOf("") }
    var durationMonths by remember { mutableIntStateOf(3) }

    LaunchedEffect(openState) {
        if (openState is OpenDepositState.Success) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it; viewModel.resetOpenDepositState() },
                label = { Text("Сума депозиту") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Оберіть термін:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            val durations = listOf(
                Triple(3, "3 місяці", "8%"),
                Triple(6, "6 місяців", "11%"),
                Triple(12, "12 місяців", "14%")
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                durations.forEach { (months, label, percent) ->
                    FilterChip(
                        selected = durationMonths == months,
                        onClick = { durationMonths = months },
                        label = { Text("$label ($percent)") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (openState is OpenDepositState.Error) {
                Text(text = (openState as OpenDepositState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (openState is OpenDepositState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        amount.toDoubleOrNull()?.let {
                            viewModel.openDeposit(accountId, it, durationMonths)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = amount.isNotEmpty()
                ) {
                    Text("Підтвердити відкриття")
                }
            }
        }
    }
}