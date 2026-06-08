package com.example.mobibank.di

import com.example.mobibank.feature.accounts.AccountsViewModel
import com.example.mobibank.feature.auth.AuthViewModel
import com.example.mobibank.feature.deposits.DepositsViewModel
import com.example.mobibank.feature.loans.LoansViewModel
import com.example.mobibank.feature.profile.ProfileViewModel
import com.example.mobibank.feature.transactions.TransactionsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        AuthViewModel(
            loginUseCase = get(),
            registerUseCase = get()
        )
    }

    viewModel {
        AccountsViewModel(
            getMyAccountsUseCase = get(),
            openCardUseCase = get()
        )
    }

    viewModel {
        TransactionsViewModel(
            transferMoneyUseCase = get(),
            getTransactionHistoryUseCase = get()
        )
    }

    viewModel {
        DepositsViewModel(
            getMyDepositsUseCase = get(),
            openDepositUseCase = get(),
            closeDepositUseCase = get(),
        )
    }

    viewModel {
        LoansViewModel(
            getMyLoansUseCase = get(),
            applyMyLoansUseCase = get(),
            repayLoanUseCase = get()
        )
    }

    viewModel {
        ProfileViewModel(
            tokenManager = get(),
            getProfileUseCase = get(),
            updateProfileUseCase = get(),
            changePasswordUseCase = get()
        )
    }
}