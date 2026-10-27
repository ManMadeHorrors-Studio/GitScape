package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.R

enum class AuthError (val type: String?, val message: Int? = null) {
    PENDING("authorization_pending"),
    SLOW_DOWN("slow_down"),
    DENIED("access_denied", R.string.AUTH_ERROR_DENIED),
    EXPIRED("expired_token", R.string.AUTH_ERROR_EXPIRED),
    DIFFERENT_USER(null, R.string.AUTH_ERROR_DIFFERENT_USER),
    INVALID_ACCESS_TOKEN(null, R.string.AUTH_ERROR_INVALID_TOKEN),
    INVALID_REFRESH_TOKEN("bad_refresh_token", R.string.AUTH_ERROR_INVALID_TOKEN),
    GENERIC(null, R.string.AUTH_ERROR_GENERIC);

    companion object {
        fun fromType(type: String?): AuthError {
            return entries.find { ae ->  ae.type == type } ?: GENERIC
        }
    }
}