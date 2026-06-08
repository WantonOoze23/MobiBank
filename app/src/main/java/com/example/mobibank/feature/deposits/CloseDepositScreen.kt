package com.example.mobibank.feature.deposits

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobibank.feature.accounts.AccountsListState
import com.example.mobibank.feature.accounts.AccountsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseDepositScreen(
    depositId: String,
    onNavigateBack: () -> Unit,
    depositsViewModel: DepositsViewModel = koinViewModel(),
    accountsViewModel: AccountsViewModel = koinViewModel()
) {
    val closeState by depositsViewModel.closeState.collectAsStateWithLifecycle()
    val accountsState by accountsViewModel.listState.collectAsStateWithLifecycle()

    var selectedAccountId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(closeState) {
        if (closeState is CloseDepositState.Success) {
            Toast.makeText(context, "Вклад закрито, кошти зараховано!", Toast.LENGTH_SHORT).show()
            depositsViewModel.resetCloseState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Повернення вкладу") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)
        ) {
            Text("Оберіть картку для зарахування коштів (тіло + відсотки):", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            when (accountsState) {
                is AccountsListState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is AccountsListState.Error -> Text("Помилка завантаження карток", color = MaterialTheme.colorScheme.error)
                is AccountsListState.Success -> {
                    val accounts = (accountsState as AccountsListState.Success).accounts

                    if (accounts.isEmpty()) {
                        Text("У вас немає карток для зарахування.")
                    } else {
                        LaunchedEffect(accounts) {
                            if (selectedAccountId == null) selectedAccountId = accounts.first().id
                        }

                        accounts.forEach { account ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedAccountId == account.id),
                                    onClick = { selectedAccountId = account.id }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Картка ${account.type.name} (..${account.cardNumber.takeLast(4)})")
                                    Text("Баланс: ${account.balance} ${account.currency.name}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Увага! При достроковому закритті нараховується лише 50% від фактичних відсотків. Кошти будуть автоматично конвертовані у валюту обраної картки.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            if (closeState is CloseDepositState.Error) {
                Text(text = (closeState as CloseDepositState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { selectedAccountId?.let { depositsViewModel.closeDeposit(depositId, it) } },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = selectedAccountId != null && closeState !is CloseDepositState.Loading
            ) {
                if (closeState is CloseDepositState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Підтвердити закриття")
                }
            }
        }
    }
}