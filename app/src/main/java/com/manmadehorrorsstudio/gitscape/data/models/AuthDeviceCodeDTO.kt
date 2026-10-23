package com.manmadehorrorsstudio.gitscape.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthDeviceCodeDTO(
    @Json(name = "device_code")
    val deviceCode: String? = null,
    @Json(name = "user_code")
    val userCode: String? = null,
    @Json(name = "verification_uri")
    val verificationUri: String? = null,
    @Json(name = "expires_in")
    val expiresIn: Int? = null,
    val interval: Int? = null
)

fun AuthDeviceCodeDTO.isValid(): Boolean {
    return deviceCode != null && userCode != null && verificationUri != null && expiresIn != null && interval != null
}