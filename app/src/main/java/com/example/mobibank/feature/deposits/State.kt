package com.example.mobibank.feature.deposits

import com.example.domain.model.DepositModel

sealed class DepositsListState {
    object Loading : DepositsListState()
    data class Success(val deposits: List<DepositModel>) : DepositsListState()
    data class Error(val message: String) : DepositsListState()
}

sealed class OpenDepositState {
    object Idle : OpenDepositState()
    object Loading : OpenDepositState()
    object Success : OpenDepositState()
    data class Error(val message: String) : OpenDepositState()
}