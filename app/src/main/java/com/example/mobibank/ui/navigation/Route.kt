package com.example.mobibank.ui.navigation

object Route {
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val ACCOUNTS = "accounts"
    const val OPEN_ACCOUNT = "open_account"

    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"
    const val TRANSFERS_HUB = "transfers_hub"

    const val DEPOSITS = "deposits"
    const val OPEN_DEPOSIT = "open_deposit/{accountId}"
    const val CLOSE_DEPOSIT = "close_deposit/{depositId}"
    fun openDeposit(accountId: String) = "open_deposit/$accountId"
    fun closeDeposit(depositId: String) = "close_deposit/$depositId"

    const val LOANS = "loans"
    const val APPLY_LOAN = "apply_loan/{accountId}"
    const val REPAY_LOAN = "repay_loan/{loanId}"
    fun applyLoan(accountId: String) = "apply_loan/$accountId"
    fun repayLoan(loanId: String) = "repay_loan/$loanId"

    const val HISTORY = "history/{accountId}"
    fun history(accountId: String) = "history/$accountId"

    const val TRANSFER = "transfer/{accountId}"
    fun transfer(accountId: String) = "transfer/$accountId"
}