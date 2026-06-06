package com.example.mobibank.feature.deposits


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
import com.example.domain.model.DepositModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositsScreen(
    onNavigateToOpenDeposit: () -> Unit,
    viewModel: DepositsViewModel = koinViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої депозити", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToOpenDeposit) {
                Icon(Icons.Default.Add, contentDescription = "Відкрити депозит")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            when (listState) {
                is DepositsListState.Loading -> CircularProgressIndicator()
                is DepositsListState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = (listState as DepositsListState.Error).message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDeposits() }) { Text("Оновити") }
                    }
                }
                is DepositsListState.Success -> {
                    val deposits = (listState as DepositsListState.Success).deposits
                    if (deposits.isEmpty()) {
                        Text("У вас немає відкритих депозитів", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(deposits) { deposit ->
                                DepositCardItem(deposit)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DepositCardItem(deposit: DepositModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Вклад до ${deposit.endDate}", style = MaterialTheme.typography.titleMedium)
                Text(text = deposit.status.name, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${deposit.amount} ${deposit.currency.name}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ставка: ${deposit.interestRate}% річних", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Відкрито: ${deposit.startDate}", style = MaterialTheme.typography.labelSmall)
        }
    }
}