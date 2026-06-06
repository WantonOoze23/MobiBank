package com.example.mobibank.feature.deposits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.deposits.GetMyDepositsUseCase
import com.example.domain.usecase.deposits.OpenDepositUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepositsViewModel(
    private val getMyDepositsUseCase: GetMyDepositsUseCase,
    private val openDepositUseCase: OpenDepositUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow<DepositsListState>(DepositsListState.Loading)
    val listState : StateFlow<DepositsListState> = _listState.asStateFlow()

    private val _openState = MutableStateFlow<OpenDepositState>(OpenDepositState.Idle)
    val openState : StateFlow<OpenDepositState> = _openState.asStateFlow()

    init {
        loadDeposits()
    }

    fun loadDeposits(){
        viewModelScope.launch {
            _listState.value = DepositsListState.Loading

            val result = getMyDepositsUseCase()

            result.onSuccess { deposits ->
                _listState.value = DepositsListState.Success(deposits)
            }.onFailure { exception ->
                _listState.value = DepositsListState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun openDeposit(userId: String, amount: Double, durationMonths: Int){
        viewModelScope.launch {
            _openState.value = OpenDepositState.Loading

            val result = openDepositUseCase(
                userId = userId,
                amount = amount,
                durationMonths = durationMonths
            )

            result.onSuccess {
                _openState.value = OpenDepositState.Success
                loadDeposits()
            }.onFailure { exception ->
                _openState.value = OpenDepositState.Error(exception.message ?: "Unknown error")
            }
        }
    }

    fun resetOpenDepositState(){
        _openState.value = OpenDepositState.Idle
    }

}