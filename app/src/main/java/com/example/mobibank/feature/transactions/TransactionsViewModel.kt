package com.example.mobibank.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.TransactionType
import com.example.domain.usecase.transactions.GetTransactionHistoryUseCase
import com.example.domain.usecase.transactions.TransferMoneyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val transferMoneyUseCase: TransferMoneyUseCase,
    private val getTransactionHistoryUseCase: GetTransactionHistoryUseCase
) : ViewModel() {
    private val _historyState = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val historyState: StateFlow<HistoryState> = _historyState.asStateFlow()

    private val _transferState = MutableStateFlow<TransferState>(TransferState.Idle)
    val transferState: StateFlow<TransferState> = _transferState.asStateFlow()

    fun loadHistory(accountId: String, filterType: TransactionType? = null) {
        viewModelScope.launch {
            _historyState.value = HistoryState.Loading
            val result = getTransactionHistoryUseCase(accountId, filterType)

            result.onSuccess { transactions ->
                _historyState.value = HistoryState.Success(transactions)
            }.onFailure { exception ->
                _historyState.value = HistoryState.Error(exception.message ?: "Помилка завантаження історії")
            }
        }
    }

    fun transfer(senderAccountId: String, receiverCardNumber: String, amount: Double) {
        viewModelScope.launch {
            _transferState.value = TransferState.Loading
            val result = transferMoneyUseCase(senderAccountId, receiverCardNumber, amount)

            result.onSuccess {
                _transferState.value = TransferState.Success
            }.onFailure { exception ->
                _transferState.value = TransferState.Error(exception.message ?: "Не вдалося виконати переказ")
            }
        }
    }

    fun resetTransferState() {
        _transferState.value = TransferState.Idle
    }
}