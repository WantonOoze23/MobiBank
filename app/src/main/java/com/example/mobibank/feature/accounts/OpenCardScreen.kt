package com.example.mobibank.feature.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.AccountType
import com.example.domain.model.Currency
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenAccountScreen(
    onNavigateBack: () -> Unit,
    viewModel: AccountsViewModel = koinViewModel()
) {
    val openCardState by viewModel.openCardState.collectAsStateWithLifecycle()

    var selectedCurrency by remember { mutableStateOf(Currency.UAH) }

    LaunchedEffect(openCardState) {
        if (openCardState is OpenCardState.Success) {
            viewModel.resetState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Відкрити картку") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Оберіть валюту картки", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))

            Currency.values().forEach { currency ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (currency == selectedCurrency),
                        onClick = { selectedCurrency = currency }
                    )
                    Text(text = currency.name, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (openCardState is OpenCardState.Error) {
                Text(
                    text = (openCardState as OpenCardState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (openCardState is OpenCardState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.openNewCard(selectedCurrency, AccountType.DEBIT) },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Відкрити картку")
                }
            }
        }
    }
}