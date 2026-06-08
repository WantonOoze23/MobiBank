package com.example.mobibank.feature.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
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
import com.example.mobibank.feature.accounts.AccountsListState
import com.example.mobibank.feature.accounts.AccountsViewModel
import com.example.mobibank.ui.components.BottomNavigationBar
import com.example.mobibank.ui.navigation.Route
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransfersHubScreen(
    onNavigateToTransfer: (String) -> Unit,
    onBottomNavClick: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: AccountsViewModel = koinViewModel()
) {
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.loadAccounts(isSilent = true)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(15000L)
            viewModel.loadAccounts(isSilent = true)
        }
    }

    val refresh = { viewModel.loadAccounts(isSilent = false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Перекази", fontWeight = FontWeight.Bold) },
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
                currentRoute = Route.TRANSFERS_HUB,
                onNavigate = onBottomNavClick
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = listState is AccountsListState.Loading,
            onRefresh = refresh,
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (listState) {
                    is AccountsListState.Loading -> CircularProgressIndicator()

                    is AccountsListState.Error -> {
                        Text(
                            text = (listState as AccountsListState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    is AccountsListState.Success -> {
                        val accounts = (listState as AccountsListState.Success).accounts

                        if (accounts.isEmpty()) {
                            Text("У вас немає карток для переказу.")
                        } else {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = "Оберіть картку для списання:",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )

                                LazyColumn(
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(accounts) { account ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onNavigateToTransfer(account.id) },
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CreditCard,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(32.dp)
                                                )
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Column {
                                                    Text(
                                                        text = "Картка ${account.type.name} (..${account.cardNumber.takeLast(4)})",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Text(
                                                        text = "Баланс: ${account.balance} ${account.currency.name}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            }
        }

    }
}