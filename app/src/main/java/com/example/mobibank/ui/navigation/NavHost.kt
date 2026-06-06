package com.example.mobibank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.mobibank.feature.auth.LoginScreen
import com.example.mobibank.feature.auth.RegisterScreen
import com.example.mobibank.feature.accounts.AccountsScreen
import com.example.mobibank.feature.accounts.OpenAccountScreen
import com.example.mobibank.feature.deposits.DepositsScreen
import com.example.mobibank.feature.deposits.OpenDepositScreen
import com.example.mobibank.feature.loans.LoansScreen
import com.example.mobibank.feature.loans.ApplyLoanScreen
import com.example.mobibank.feature.transactions.HistoryScreen
import com.example.mobibank.feature.transactions.TransferScreen

@Composable
fun MobiBankNavHost(
    navController: NavHostController,
    startDestination: String = Route.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ================= AUTH =================
        composable(Route.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Route.REGISTER) },
                onNavigateToHome = {
                    navController.navigate(Route.ACCOUNTS) {
                        popUpTo(Route.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.REGISTER) {
            RegisterScreen(
                OnNavigateToLogin = { navController.popBackStack() },
                OnNavigateToHome = {
                    navController.navigate(Route.ACCOUNTS) {
                        popUpTo(Route.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // ================= ACCOUNTS =================
        composable(Route.ACCOUNTS) {
            AccountsScreen(
                onNavigateToOpenCard = { navController.navigate(Route.OPEN_ACCOUNT) },
                onNavigateToHistory = { accountId -> navController.navigate(Route.history(accountId)) },
                onNavigateToTransfer = { accountId -> navController.navigate(Route.transfer(accountId)) },
                onNavigateToOpenDeposit = { accountId -> navController.navigate(Route.openDeposit(accountId)) },
                onNavigateToApplyLoan = { accountId -> navController.navigate(Route.applyLoan(accountId)) },
                onNavigateToAllDeposits = { navController.navigate(Route.DEPOSITS) },
                onNavigateToAllLoans = { navController.navigate(Route.LOANS) }
            )
        }

        composable(Route.OPEN_ACCOUNT) {
            OpenAccountScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ================= DEPOSITS =================
        composable(Route.DEPOSITS) {
            DepositsScreen(
                // Оскільки відкриття депозиту вимагає ID картки,
                // з екрану загальних депозитів ми повертаємо користувача на головний екран для вибору картки
                onNavigateToOpenDeposit = { navController.navigate(Route.ACCOUNTS) }
            )
        }

        composable(
            route = Route.OPEN_DEPOSIT,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            OpenDepositScreen(
                accountId = accountId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ================= LOANS =================
        composable(Route.LOANS) {
            LoansScreen(
                // Аналогічно: щоб взяти кредит, треба повернутися і обрати картку
                onNavigateToApplyLoan = { navController.navigate(Route.ACCOUNTS) }
            )
        }

        composable(
            route = Route.APPLY_LOAN,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            ApplyLoanScreen(
                accountId = accountId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ================= TRANSACTIONS =================
        composable(
            route = Route.HISTORY,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            HistoryScreen(
                accountId = accountId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.TRANSFER,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            TransferScreen(
                senderAccountId = accountId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}