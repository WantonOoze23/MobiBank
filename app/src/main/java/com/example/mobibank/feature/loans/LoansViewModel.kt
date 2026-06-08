package com.example.mobibank.feature.loans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.loans.ApplyForLoanUseCase
import com.example.domain.usecase.loans.GetMyLoansUseCase
import com.example.domain.usecase.loans.RepayLoanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoansViewModel(
    private val getMyLoansUseCase: GetMyLoansUseCase,
    private val applyMyLoansUseCase: ApplyForLoanUseCase,
    private val repayLoanUseCase: RepayLoanUseCase
) : ViewModel() {
    private val _listState = MutableStateFlow<LoansListState>(LoansListState.Loading)
    val listState : StateFlow<LoansListState> = _listState.asStateFlow()

    private val _applyState = MutableStateFlow<ApplyLoanState>(ApplyLoanState.Idle)
    val applyState : StateFlow<ApplyLoanState> = _applyState.asStateFlow()

    private val _repayState = MutableStateFlow<RepayLoanState>(RepayLoanState.Idle)
    val repayState: StateFlow<RepayLoanState> = _repayState.asStateFlow()

    init {
        loadLoans(false)
    }

    fun loadLoans(isSilent: Boolean = false){
        viewModelScope.launch {
            if (!isSilent) {
                _listState.value = LoansListState.Loading
            }

            val result = getMyLoansUseCase()

            result.onSuccess { loans ->
                _listState.value = LoansListState.Success(loans)
            }.onFailure { exception ->
                if (!isSilent || _listState.value !is LoansListState.Success) {
                    _listState.value = LoansListState.Error(exception.message ?: "Unknown error")
                }
            }
        }
    }

    fun applyForLoan(accountId: String, amount: Double, termInDays: Int){
        viewModelScope.launch {
            _applyState.value = ApplyLoanState.Loading

            val result = applyMyLoansUseCase(
                accountId = accountId,
                amount = amount,
                termInDays = termInDays
            )

            result.onSuccess {
                _applyState.value = ApplyLoanState.Success
                loadLoans(true)
            }.onFailure { exception ->
                _applyState.value = ApplyLoanState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun repayLoan(loanId: String, accountId: String) {
        viewModelScope.launch {
            _repayState.value = RepayLoanState.Loading
            val result = repayLoanUseCase(loanId, accountId)

            result.onSuccess {
                _repayState.value = RepayLoanState.Success
                loadLoans(true)
            }.onFailure { exception ->
                _repayState.value = RepayLoanState.Error(exception.message ?: "Помилка погашення")
            }
        }
    }

    fun resetRepayState() {
        _repayState.value = RepayLoanState.Idle
    }

    fun resetApplyState() {
        _applyState.value = ApplyLoanState.Idle
    }
}