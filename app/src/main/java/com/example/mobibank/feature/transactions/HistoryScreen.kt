package com.example.mobibank.feature.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.TransactionModel
import com.example.domain.model.TransactionType
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    accountId: String, // Передається через навігацію
    onNavigateBack: () -> Unit,
    viewModel: TransactionsViewModel = koinViewModel()
) {
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf<TransactionType?>(null) }

    // Завантажуємо історію при відкритті екрану або зміні фільтра
    LaunchedEffect(accountId, selectedFilter) {
        viewModel.loadHistory(accountId, selectedFilter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Історія транзакцій") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("Всі") }
                )
                FilterChip(
                    selected = selectedFilter == TransactionType.INCOME,
                    onClick = { selectedFilter = TransactionType.INCOME },
                    label = { Text("Надходження") }
                )
                FilterChip(
                    selected = selectedFilter == TransactionType.EXPENSE,
                    onClick = { selectedFilter = TransactionType.EXPENSE },
                    label = { Text("Витрати") }
                )
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when (historyState) {
                    is HistoryState.Loading -> CircularProgressIndicator()
                    is HistoryState.Error -> {
                        Text(text = (historyState as HistoryState.Error).message, color = MaterialTheme.colorScheme.error)
                    }
                    is HistoryState.Success -> {
                        val transactions = (historyState as HistoryState.Success).transactions
                        if (transactions.isEmpty()) {
                            Text("Транзакцій не знайдено", style = MaterialTheme.typography.bodyLarge)
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                                items(transactions) { tx ->
                                    TransactionItem(tx)
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionModel) {
    val isIncome = transaction.type == TransactionType.INCOME
    val amountColor = if (isIncome) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error // Зелений або червоний
    val sign = if (isIncome) "+" else "-"

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = if (isIncome) "Поповнення" else "Переказ",
                style = MaterialTheme.typography.titleMedium
            )
            transaction.counterpartyCard?.let {
                Text(text = "Картка: $it", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(text = transaction.timestamp.take(10), style = MaterialTheme.typography.labelSmall, color = Color.Gray) // Показуємо тільки дату
        }

        Text(
            text = "$sign${transaction.amount} ₴",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}