package com.example.mobibank.feature.transactions

import com.example.domain.model.TransactionModel

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val transactions: List<TransactionModel>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

sealed class TransferState {
    object Idle : TransferState()
    object Loading : TransferState()
    object Success : TransferState()
    data class Error(val message: String) : TransferState()
}