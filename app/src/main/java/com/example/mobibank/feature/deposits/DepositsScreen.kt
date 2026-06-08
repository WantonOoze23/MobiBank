package com.example.mobibank.feature.deposits


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
import com.example.domain.model.DepositModel
import com.example.mobibank.feature.loans.LoansListState
import com.example.mobibank.ui.components.BottomNavigationBar
import com.example.mobibank.ui.navigation.Route
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositsScreen(
    onNavigateToOpenDeposit: () -> Unit,
    onNavigateToClose: (String) -> Unit,
    onBottomNavClick: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: DepositsViewModel = koinViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.loadDeposits(isSilent = true)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(15000L)
            viewModel.loadDeposits(isSilent = true)
        }
    }

    val refresh = { viewModel.loadDeposits(isSilent = false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мої депозити", fontWeight = FontWeight.Bold) },
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
                currentRoute = Route.DEPOSITS,
                onNavigate = onBottomNavClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToOpenDeposit) {
                Icon(Icons.Default.Add, contentDescription = "Відкрити депозит")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = listState is DepositsListState.Loading,
            onRefresh = refresh,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center
            ) {
                when (listState) {
                    is DepositsListState.Loading -> CircularProgressIndicator()
                    is DepositsListState.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (listState as DepositsListState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadDeposits() }) { Text("Оновити") }
                        }
                    }

                    is DepositsListState.Success -> {
                        val deposits = (listState as DepositsListState.Success).deposits
                        if (deposits.isEmpty()) {
                            Text(
                                "У вас немає відкритих депозитів",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(deposits) { deposit ->
                                    DepositCardItem(deposit, { onNavigateToClose(deposit.id) })
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
fun DepositCardItem(deposit: DepositModel, onCloseClick: () -> Unit) {
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
            Spacer(modifier = Modifier.height(16.dp))

            if (deposit.status.name == "ACTIVE") {
                Button(
                    onClick = onCloseClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Закрити та повернути кошти")
                }
            }
        }
    }
}