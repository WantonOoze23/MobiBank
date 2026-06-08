package com.example.mobibank.feature.accounts

import com.example.domain.model.AccountModel
import com.example.domain.model.TransactionModel

sealed class AccountsListState {
    object Loading : AccountsListState()
    data class Success(val accounts: List<AccountModel>) : AccountsListState()
    data class Error(val message: String) : AccountsListState()
}

sealed class OpenCardState {
    object Idle : OpenCardState()
    object Loading : OpenCardState()
    object Success : OpenCardState()
    data class Error(val message: String) : OpenCardState()
}

sealed class RecentTransactionsState {
    object Idle : RecentTransactionsState()
    object Loading : RecentTransactionsState()
    data class Success(val transactions: List<TransactionModel>) : RecentTransactionsState()
    data class Error(val message: String) : RecentTransactionsState()
}