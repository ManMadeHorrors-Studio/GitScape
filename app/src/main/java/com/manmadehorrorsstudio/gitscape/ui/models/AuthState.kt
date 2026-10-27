package com.manmadehorrorsstudio.gitscape.ui.models

import com.manmadehorrorsstudio.gitscape.domain.models.AuthDeviceCode
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser

data class AuthState (
    val authDeviceCode: AuthDeviceCode? = null,
    val gitHubUser: GitHubUser? = null,
    val authStatus: AuthStatus = AuthStatus.STEP1,
    val isLoading: Boolean = true
)