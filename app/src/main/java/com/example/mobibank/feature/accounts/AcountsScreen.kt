package com.example.mobibank.feature.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.domain.model.AccountModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = koinViewModel(),
    onNavigateToOpenCard: () -> Unit,
    onNavigateToHistory: (String) -> Unit,
    onNavigateToTransfer: (String) -> Unit,
    onNavigateToOpenDeposit: (String) -> Unit,
    onNavigateToApplyLoan: (String) -> Unit,
    onNavigateToAllDeposits: () -> Unit,
    onNavigateToAllLoans: () -> Unit
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    var isRefreshing by remember { mutableStateOf(false) }

    val refresh = {
        viewModel.loadAccounts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої рахунки", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    TextButton(onClick = onNavigateToAllDeposits) { Text("Депозити") }
                    TextButton(onClick = onNavigateToAllLoans) { Text("Кредити") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToOpenCard) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Відкрити картку")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = listState is AccountsListState.Loading,
            onRefresh = refresh
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (listState) {
                    is AccountsListState.Loading -> CircularProgressIndicator()
                    is AccountsListState.Error -> {
                        val error = (listState as AccountsListState.Error).message
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = error, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadAccounts() }) { Text("Спробувати ще раз") }
                        }
                    }

                    is AccountsListState.Success -> {
                        val accounts = (listState as AccountsListState.Success).accounts
                        if (accounts.isEmpty()) {
                            Text(
                                "У вас ще немає карток. Відкрийте першу!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(accounts) { account ->
                                    // ДОДАНО: передаємо колбеки в карточку
                                    AccountCardItem(
                                        account = account,
                                        onHistoryClick = { onNavigateToHistory(account.id) },
                                        onTransferClick = { onNavigateToTransfer(account.id) },
                                        onDepositClick = { onNavigateToOpenDeposit(account.id) },
                                        onLoanClick = { onNavigateToApplyLoan(account.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class OutputCurrency(
    val dollar : String = "$",
    val euro : String = "€",
    val uah : String = "₴"
)

@Composable
fun AccountCardItem(
    account: AccountModel,
    onHistoryClick: () -> Unit,
    onTransferClick: () -> Unit,
    onDepositClick: () -> Unit,
    onLoanClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Картка ${account.type.name}", style = MaterialTheme.typography.titleMedium)
                Text(text = account.status.name, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${if (account.currency == Currency.USD) OutputCurrency().dollar else if (account.currency == Currency.EUR) OutputCurrency().euro else OutputCurrency().uah} ${account.balance}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            val maskedCard = account.cardNumber.chunked(4).joinToString(" ")
            Text(text = maskedCard, style = MaterialTheme.typography.bodyLarge, letterSpacing = 2.sp)

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onTransferClick) { Text("Переказ") }
                TextButton(onClick = onHistoryClick) { Text("Історія") }
                TextButton(onClick = onDepositClick) { Text("Депозит") }
                TextButton(onClick = onLoanClick) { Text("Кредит") }
            }
        }
    }
}