package com.example.mobibank.feature.loans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.LoanModel
import com.example.mobibank.ui.components.BottomNavigationBar
import com.example.mobibank.ui.navigation.Route
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoansScreen(
    onNavigateToApplyLoan: () -> Unit,
    onNavigateToRepay: (String) -> Unit,
    onBottomNavClick: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: LoansViewModel = koinViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.loadLoans(isSilent = true)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(15000L)
            viewModel.loadLoans(isSilent = true)
        }
    }

    val refresh = { viewModel.loadLoans(isSilent = false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої кредити", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Профіль")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = Route.LOANS,
                onNavigate = onBottomNavClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToApplyLoan) {
                Icon(Icons.Default.Add, contentDescription = "Взяти кредит")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = listState is LoansListState.Loading,
            onRefresh = refresh,
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (listState) {
                    is LoansListState.Loading -> CircularProgressIndicator()
                    is LoansListState.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (listState as LoansListState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadLoans() }) { Text("Оновити") }
                        }
                    }

                    is LoansListState.Success -> {
                        val loans = (listState as LoansListState.Success).loans
                        if (loans.isEmpty()) {
                            Text(
                                "У вас немає активних кредитів 🎉",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(loans) { loan ->
                                    LoanCardItem(
                                        loan = loan,
                                        onRepayClick = { onNavigateToRepay(loan.id) })
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
fun LoanCardItem(loan: LoanModel, onRepayClick: () -> Unit) {

    val currencySymbol = when (loan.currency) {
        "USD" -> "$"
        "EUR" -> "€"
        else -> "₴"
    }

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
                text = "До сплати: ${loan.remainingAmount} $currencySymbol",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Тіло кредиту: ${loan.amount} $currencySymbol (Ставка 5%)",
                style = MaterialTheme.typography.bodyMedium
            )

            if (loan.status.name == "ACTIVE") {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRepayClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Погасити кредит")
                }
            }
        }
    }
}