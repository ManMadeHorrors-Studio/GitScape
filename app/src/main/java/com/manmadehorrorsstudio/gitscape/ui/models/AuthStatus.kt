package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.R

enum class AuthStatus(val message: Int?, val buttonMessage: Int? = null) {
    STEP1(R.string.AUTH_WELCOME_STEP1, R.string.AUTH_WELCOME_BUTTON1),
    STEP2(R.string.AUTH_WELCOME_STEP2, R.string.AUTH_WELCOME_BUTTON2),
    BROWSING(R.string.AUTH_WELCOME_BROWSING, R.string.GENERIC_ERROR_CANCEL),
    SUCCESS(R.string.AUTH_WELCOME_SUCCESS, R.string.AUTH_WELCOME_SUCCESS_BUTTON),
    VALIDATED(null),
    OFFLINE(null)
}