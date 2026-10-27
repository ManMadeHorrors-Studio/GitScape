package com.manmadehorrorsstudio.gitscape.domain.models

data class AuthDeviceCode (
    val deviceCode: String,
    val userCode: String,
    val verificationUri: String,
    val expiresAt: Long,
    val interval: Int
)