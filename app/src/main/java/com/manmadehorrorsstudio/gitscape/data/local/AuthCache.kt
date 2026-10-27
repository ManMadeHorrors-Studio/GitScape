package com.manmadehorrorsstudio.gitscape.data.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthCache @Inject constructor() {

    @Volatile
    private var accessToken: String? = null

    fun getAccessToken(): String? = accessToken

    fun updateAccessToken(token: String) {
        accessToken = token
    }

    fun clearCache() {
        accessToken = null
    }
}