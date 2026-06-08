package com.example.mobibank.feature.loans

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

data class LoanTermOption(val label: String, val days: Int)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLoanScreen(
    accountId: String, // ID картки, на яку беремо кредит
    onNavigateBack: () -> Unit,
    viewModel: LoansViewModel = koinViewModel()
) {
    val applyState by viewModel.applyState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var amountText by remember { mutableStateOf("") }

    val termOptions = listOf(
        LoanTermOption("30 днів", 30),
        LoanTermOption("3 місяці", 90),
        LoanTermOption("6 місяців", 180),
        LoanTermOption("12 місяців", 365),
        LoanTermOption("24 місяці", 730)
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedTerm by remember { mutableStateOf(termOptions[0]) }

    LaunchedEffect(applyState) {
        if (applyState is ApplyLoanState.Success) {
            Toast.makeText(context, "Кредит успішно оформлено!", Toast.LENGTH_SHORT).show()
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
            Text("Ставка: 5% річних", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Бажана сума") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Випадаючий список для вибору терміну
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedTerm.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Термін кредиту") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    termOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                selectedTerm = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (applyState is ApplyLoanState.Error) {
                Text(text = (applyState as ApplyLoanState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.applyForLoan(accountId, amount, selectedTerm.days)
                    } else {
                        Toast.makeText(context, "Введіть коректну суму", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = applyState !is ApplyLoanState.Loading && amountText.isNotBlank()
            ) {
                if (applyState is ApplyLoanState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Отримати кошти")
                }
            }
        }
    }
}