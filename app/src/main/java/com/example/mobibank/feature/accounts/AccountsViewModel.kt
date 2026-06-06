package com.example.mobibank.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AccountType
import com.example.domain.model.Currency
import com.example.domain.usecase.accounts.GetMyAccountsUseCase
import com.example.domain.usecase.accounts.OpenCardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountsViewModel(
    private val getMyAccountsUseCase: GetMyAccountsUseCase,
    private val openCardUseCase: OpenCardUseCase
) : ViewModel() {
    private val _listState = MutableStateFlow<AccountsListState>(AccountsListState.Loading)
    val listState: StateFlow<AccountsListState> = _listState.asStateFlow()

    private val _openCardState = MutableStateFlow<OpenCardState>(OpenCardState.Idle)
    val openCardState: StateFlow<OpenCardState> = _openCardState.asStateFlow()

    init {
        loadAccounts()
    }

    fun loadAccounts(){
        viewModelScope.launch {
            _listState.value = AccountsListState.Loading
            val result = getMyAccountsUseCase()

            result.onSuccess { accounts ->
                _listState.value = AccountsListState.Success(accounts)
            }.onFailure { exception ->
                _listState.value = AccountsListState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun openNewCard(currency: Currency, type: AccountType){
        viewModelScope.launch {
            _openCardState.value = OpenCardState.Loading
            val result = openCardUseCase(currency, type)

            result.onSuccess {
                _openCardState.value = OpenCardState.Success
                loadAccounts()
            }.onFailure { exception ->
                _openCardState.value = OpenCardState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun resetState(){
        _openCardState.value = OpenCardState.Idle
    }
}