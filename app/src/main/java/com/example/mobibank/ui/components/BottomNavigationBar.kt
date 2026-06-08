package com.example.mobibank.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mobibank.ui.navigation.Route

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.CreditCard, contentDescription = "Рахунки") },
            label = { Text("Рахунки") },
            selected = currentRoute == Route.ACCOUNTS,
            onClick = { onNavigate(Route.ACCOUNTS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.SwapHoriz, contentDescription = "Перекази") },
            label = { Text("Перекази") },
            selected = currentRoute == Route.TRANSFERS_HUB,
            onClick = { onNavigate(Route.TRANSFERS_HUB) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Депозити") },
            label = { Text("Депозити") },
            selected = currentRoute == Route.DEPOSITS,
            onClick = { onNavigate(Route.DEPOSITS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Money, contentDescription = "Кредити") },
            label = { Text("Кредити") },
            selected = currentRoute == Route.LOANS,
            onClick = { onNavigate(Route.LOANS) }
        )
    }
}