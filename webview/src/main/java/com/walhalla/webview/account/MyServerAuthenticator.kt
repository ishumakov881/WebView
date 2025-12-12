package com.walhalla.webview.account

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date


class MyServerAuthenticator : IServerAuthenticator {
    override fun signUp(email: String, username: String, password: String): String? {
        // TODO: register new user on the server and return its auth token
        return null
    }

    override fun signIn(email: String, password: String): String? {
        var authToken: String? = null
        val df: DateFormat = SimpleDateFormat("yyyyMMdd-HHmmss")

        if (mCredentialsRepo.containsKey(email)) {
            if (password == mCredentialsRepo[email]) {
                authToken = email + "-" + df.format(Date())
            }
        }

        return authToken
    }

    companion object {
        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val mCredentialsRepo: Map<String, String>

        init {
            val credentials: MutableMap<String, String> = HashMap()
            credentials["demo@example.com"] = "demo"
            credentials["foo@example.com"] = "foobar"
            credentials["user@example.com"] = "pass"
            mCredentialsRepo = Collections.unmodifiableMap(credentials)
        }
    }
}