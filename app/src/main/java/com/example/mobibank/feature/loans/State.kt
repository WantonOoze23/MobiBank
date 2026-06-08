package com.example.mobibank.feature.loans

import com.example.domain.model.LoanModel

sealed class LoansListState {
    object Loading : LoansListState()
    data class Success(val loans: List<LoanModel>) : LoansListState()
    data class Error(val message: String) : LoansListState()
}

sealed class ApplyLoanState {
    object Idle : ApplyLoanState()
    object Loading : ApplyLoanState()
    object Success : ApplyLoanState()
    data class Error(val message: String) : ApplyLoanState()
}

sealed class RepayLoanState {
    object Idle : RepayLoanState()
    object Loading : RepayLoanState()
    object Success : RepayLoanState()
    data class Error(val message: String) : RepayLoanState()
}