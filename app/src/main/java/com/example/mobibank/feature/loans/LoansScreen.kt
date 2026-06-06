package com.example.mobibank.feature.loans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.LoanModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoansScreen(
    onNavigateToApplyLoan: () -> Unit,
    viewModel: LoansViewModel = koinViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої кредити", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToApplyLoan) {
                Icon(Icons.Default.Add, contentDescription = "Взяти кредит")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            when (listState) {
                is LoansListState.Loading -> CircularProgressIndicator()
                is LoansListState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = (listState as LoansListState.Error).message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadLoans() }) { Text("Оновити") }
                    }
                }
                is LoansListState.Success -> {
                    val loans = (listState as LoansListState.Success).loans
                    if (loans.isEmpty()) {
                        Text("У вас немає активних кредитів 🎉", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(loans) { loan ->
                                LoanCardItem(loan)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoanCardItem(loan: LoanModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (loan.status.name == "ACTIVE") MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Погасити до ${loan.dueDate}", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = loan.status.name,
                    color = if (loan.status.name == "ACTIVE") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "До сплати: ${loan.remainingAmount} ₴",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Тіло кредиту: ${loan.amount} ₴ (Ставка 5%)", style = MaterialTheme.typography.bodyMedium)
        }
    }
}