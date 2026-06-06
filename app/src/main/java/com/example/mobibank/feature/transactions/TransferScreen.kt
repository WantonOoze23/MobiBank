package com.example.mobibank.feature.transactions


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
fun TransferScreen(
    senderAccountId: String,
    onNavigateBack: () -> Unit,
    viewModel: TransactionsViewModel = koinViewModel()
) {
    val transferState by viewModel.transferState.collectAsStateWithLifecycle()

    var receiverCard by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    LaunchedEffect(transferState) {
        if (transferState is TransferState.Success) {
            viewModel.resetTransferState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Переказ коштів") },
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
                value = receiverCard,
                onValueChange = {
                    if (it.length <= 16) receiverCard = it
                    viewModel.resetTransferState()
                },
                label = { Text("Картка отримувача (16 цифр)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    viewModel.resetTransferState()
                },
                label = { Text("Сума") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (transferState is TransferState.Error) {
                Text(text = (transferState as TransferState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (transferState is TransferState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        val amountDouble = amount.toDoubleOrNull()
                        if (amountDouble != null) {
                            viewModel.transfer(senderAccountId, receiverCard, amountDouble)
                        } else {
                            // Клієнтська валідація на некоректний ввід
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = receiverCard.length == 16 && amount.isNotEmpty()
                ) {
                    Text("Надіслати")
                }
            }
        }
    }
}