package com.example.mobibank.ui.navigation

object Route {
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val ACCOUNTS = "accounts"
    const val OPEN_ACCOUNT = "open_account"

    const val DEPOSITS = "deposits"
    const val OPEN_DEPOSIT = "open_deposit/{accountId}"
    fun openDeposit(accountId: String) = "open_deposit/$accountId"

    const val LOANS = "loans"
    const val APPLY_LOAN = "apply_loan/{accountId}"
    fun applyLoan(accountId: String) = "apply_loan/$accountId"

    const val HISTORY = "history/{accountId}"
    fun history(accountId: String) = "history/$accountId"

    const val TRANSFER = "transfer/{accountId}"
    fun transfer(accountId: String) = "transfer/$accountId"
}