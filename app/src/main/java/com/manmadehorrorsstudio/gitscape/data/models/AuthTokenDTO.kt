package com.manmadehorrorsstudio.gitscape.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenDTO(
    @Json(name = "access_token")
    val accessToken: String? = null,

    @Json(name = "refresh_token")
    val refreshToken: String? = null,

    @Json(name = "expires_in")
    val accessTokenExpiresIn: Long? = null,

    @Json(name = "refresh_token_expires_in")
    val refreshTokenExpiresIn: Long? = null,

    @Json(name = "token_type")
    val tokenType: String? = null,

    val scope: String? = null,

    //questi 2 secondo me non ci sono
    val error: String? = null,

    @Json(name = "error_description")
    val errorDescription: String? = null
)

fun AuthTokenDTO.isValid(): Boolean {
    return !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()
}