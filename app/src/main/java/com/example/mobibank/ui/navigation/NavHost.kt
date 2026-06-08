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
import com.example.mobibank.feature.deposits.CloseDepositScreen
import com.example.mobibank.feature.deposits.DepositsScreen
import com.example.mobibank.feature.deposits.OpenDepositScreen
import com.example.mobibank.feature.loans.LoansScreen
import com.example.mobibank.feature.loans.ApplyLoanScreen
import com.example.mobibank.feature.loans.RepayLoanScreen
import com.example.mobibank.feature.profile.ProfileScreen
import com.example.mobibank.feature.transactions.HistoryScreen
import com.example.mobibank.feature.transactions.TransferScreen
import com.example.mobibank.feature.transactions.TransfersHubScreen

@Composable
fun MobiBankNavHost(
    navController: NavHostController,
    startDestination: String = Route.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // AUTH
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

        // ACCOUNTS
        composable(Route.ACCOUNTS) {
            AccountsScreen(
                onNavigateToOpenCard = { navController.navigate(Route.OPEN_ACCOUNT) },
                onNavigateToHistory = { accountId -> navController.navigate(Route.history(accountId)) },
                onNavigateToTransfer = { accountId ->
                    navController.navigate(
                        Route.transfer(
                            accountId
                        )
                    )
                },
                onNavigateToOpenDeposit = { accountId ->
                    navController.navigate(
                        Route.openDeposit(
                            accountId
                        )
                    )
                },
                onNavigateToApplyLoan = { accountId ->
                    navController.navigate(
                        Route.applyLoan(
                            accountId
                        )
                    )
                },
                onNavigateToAllDeposits = { navController.navigate(Route.DEPOSITS) },
                onNavigateToAllLoans = { navController.navigate(Route.LOANS) },
                onNavigateToProfile = { navController.navigate(Route.PROFILE) },

                onBottomNavClick = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.ACCOUNTS) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(Route.OPEN_ACCOUNT) {
            OpenAccountScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // DEPOSITS
        composable(Route.DEPOSITS) {
            DepositsScreen(
                onNavigateToOpenDeposit = { navController.navigate(Route.ACCOUNTS) },
                onNavigateToClose = { depositId ->
                    navController.navigate(
                        Route.closeDeposit(
                            depositId
                        )
                    )
                },
                onBottomNavClick = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.ACCOUNTS) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = { navController.navigate(Route.PROFILE) },
            )
        }

        composable(Route.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
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

        composable(
            route = Route.CLOSE_DEPOSIT,
            arguments = listOf(navArgument("depositId") { type = NavType.StringType })
        ) { backStackEntry ->
            val depositId = backStackEntry.arguments?.getString("depositId") ?: ""
            CloseDepositScreen(
                depositId = depositId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // LOANS
        composable(Route.LOANS) {
            LoansScreen(
                onNavigateToApplyLoan = { navController.navigate(Route.ACCOUNTS) },
                onNavigateToRepay = { loanId -> navController.navigate(Route.repayLoan(loanId)) },
                onBottomNavClick = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.ACCOUNTS) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = { navController.navigate(Route.PROFILE) },
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

        composable(
            route = Route.REPAY_LOAN,
            arguments = listOf(navArgument("loanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
            RepayLoanScreen(
                loanId = loanId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // TRANSACTIONS
        composable(
            route = Route.HISTORY,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            HistoryScreen(
                accountId = accountId,
                onNavigateBack = { navController.popBackStack() },
                onBottomNavClick = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.ACCOUNTS) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            route = Route.TRANSFER,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            TransferScreen(
                accountId = accountId,
                onNavigateBack = { navController.popBackStack() },
                accountCurrency = accountId
            )
        }


        // TRANSFERS
        composable(Route.TRANSFERS_HUB) {
            TransfersHubScreen(
                onNavigateToTransfer = { accountId ->
                    navController.navigate(Route.transfer(accountId))
                },
                onBottomNavClick = { route ->
                    navController.navigate(route) {
                        popUpTo(Route.ACCOUNTS) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = { navController.navigate(Route.PROFILE) },
            )
        }
    }
}