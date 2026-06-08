package com.example.mobibank.di

import com.example.domain.usecase.accounts.GetMyAccountsUseCase
import com.example.domain.usecase.accounts.OpenCardUseCase
import com.example.domain.usecase.auth.ChangePasswordUseCase
import com.example.domain.usecase.auth.GetProfileUseCase
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.RegisterUseCase
import com.example.domain.usecase.auth.UpdateProfileUseCase
import com.example.domain.usecase.deposits.CloseDepositUseCase
import com.example.domain.usecase.deposits.GetMyDepositsUseCase
import com.example.domain.usecase.deposits.OpenDepositUseCase
import com.example.domain.usecase.loans.ApplyForLoanUseCase
import com.example.domain.usecase.loans.GetMyLoansUseCase
import com.example.domain.usecase.loans.RepayLoanUseCase
import com.example.domain.usecase.transactions.GetTransactionHistoryUseCase
import com.example.domain.usecase.transactions.TransferMoneyUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { ChangePasswordUseCase(get()) }

    factory { GetMyAccountsUseCase(get()) }
    factory { OpenCardUseCase(get()) }

    factory { GetMyLoansUseCase(get()) }
    factory { ApplyForLoanUseCase(get()) }
    factory { RepayLoanUseCase(get()) }

    factory { GetMyDepositsUseCase(get()) }
    factory { OpenDepositUseCase(get()) }
    factory { CloseDepositUseCase(get()) }

    factory { GetTransactionHistoryUseCase(get()) }
    factory { TransferMoneyUseCase(get()) }
}