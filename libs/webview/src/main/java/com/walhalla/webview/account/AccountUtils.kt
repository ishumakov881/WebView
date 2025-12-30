package com.walhalla.webview.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context

object AccountUtils {
    const val ACCOUNT_TYPE: String = "com.samugg.example"
    const val AUTH_TOKEN_TYPE: String = "com.samugg.example.aaa"

    var mServerAuthenticator: IServerAuthenticator = MyServerAuthenticator()

    fun getAccount(context: Context?, accountName: String?): Account? {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)
        for (account in accounts) {
            if (account.name.equals(accountName, ignoreCase = true)) {
                return account
            }
        }
        return null
    }
}
