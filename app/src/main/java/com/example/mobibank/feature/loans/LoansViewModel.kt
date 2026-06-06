package com.example.mobibank.feature.loans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.loans.ApplyForLoanUseCase
import com.example.domain.usecase.loans.GetMyLoansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoansViewModel(
    private val getMyLoansUseCase: GetMyLoansUseCase,
    private val applyMyLoansUseCase: ApplyForLoanUseCase
) : ViewModel() {
    private val _listState = MutableStateFlow<LoansListState>(LoansListState.Loading)
    val listState : StateFlow<LoansListState> = _listState.asStateFlow()

    private val _applyState = MutableStateFlow<ApplyLoanState>(ApplyLoanState.Idle)
    val applyState : StateFlow<ApplyLoanState> = _applyState.asStateFlow()

    init {
        loadLoans()
    }

    fun loadLoans(){
        viewModelScope.launch {
            _listState.value = LoansListState.Loading

            val result = getMyLoansUseCase()

            result.onSuccess { loans ->
                _listState.value = LoansListState.Success(loans)
            }.onFailure { exception ->
                _listState.value = LoansListState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun applyForLoan(userId : String, amount : Double){
        viewModelScope.launch {
            _applyState.value = ApplyLoanState.Loading

            val result = applyMyLoansUseCase(
                userId = userId,
                amount = amount
            )

            result.onSuccess {
                _applyState.value = ApplyLoanState.Success
                loadLoans()
            }.onFailure { exception ->
                _applyState.value = ApplyLoanState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun resetApplyState() {
        _applyState.value = ApplyLoanState.Idle
    }
}