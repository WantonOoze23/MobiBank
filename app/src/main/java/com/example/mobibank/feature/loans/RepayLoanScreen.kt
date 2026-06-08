package com.example.mobibank.feature.loans

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
fun RepayLoanScreen(
    loanId: String,
    onNavigateBack: () -> Unit,
    loansViewModel: LoansViewModel = koinViewModel(),
    accountsViewModel: AccountsViewModel = koinViewModel()
) {
    val repayState by loansViewModel.repayState.collectAsStateWithLifecycle()
    val accountsState by accountsViewModel.listState.collectAsStateWithLifecycle()

    var selectedAccountId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(repayState) {
        if (repayState is RepayLoanState.Success) {
            Toast.makeText(context, "Кредит успішно погашено!", Toast.LENGTH_SHORT).show()
            loansViewModel.resetRepayState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Погашення кредиту") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)
        ) {
            Text("Оберіть картку для списання коштів:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            when (accountsState) {
                is AccountsListState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                is AccountsListState.Error -> Text("Помилка завантаження карток", color = MaterialTheme.colorScheme.error)
                is AccountsListState.Success -> {
                    val accounts = (accountsState as AccountsListState.Success).accounts

                    if (accounts.isEmpty()) {
                        Text("У вас немає карток для погашення.")
                    } else {
                        // Якщо нічого не обрано, вибираємо першу картку за замовчуванням
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

            if (repayState is RepayLoanState.Error) {
                Text(text = (repayState as RepayLoanState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { selectedAccountId?.let { loansViewModel.repayLoan(loanId, it) } },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = selectedAccountId != null && repayState !is RepayLoanState.Loading
            ) {
                if (repayState is RepayLoanState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Підтвердити оплату")
                }
            }
        }
    }
}