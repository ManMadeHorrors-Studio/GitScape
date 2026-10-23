package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.GITHUB_CLIENT_ID
import com.manmadehorrorsstudio.gitscape.GITHUB_GET_TOKEN_PATH
import com.manmadehorrorsstudio.gitscape.GITHUB_LOGIN_PATH
import com.manmadehorrorsstudio.gitscape.data.models.AuthDeviceCodeDTO
import com.manmadehorrorsstudio.gitscape.data.models.AuthTokenDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApis {  //These apis have github.com as base path. @FormUrlEncoded is required because these apis want the fields in the body.

    @FormUrlEncoded
    @POST(GITHUB_LOGIN_PATH)
    @Headers("Accept: application/json") //forces the response in json
    suspend fun requestDeviceCode(
        @Field("client_id") clientId: String = GITHUB_CLIENT_ID,
        @Field("scope") scope: String = "offline_access"
    ): AuthDeviceCodeDTO

    @FormUrlEncoded
    @POST(GITHUB_GET_TOKEN_PATH)
    @Headers("Accept: application/json")
    suspend fun pollAccessToken(
        @Field("device_code") deviceCode: String,
        @Field("client_id") clientId: String = GITHUB_CLIENT_ID,
        @Field("grant_type") grantType: String = "urn:ietf:params:oauth:grant-type:device_code"
    ): AuthTokenDTO

    @FormUrlEncoded
    @POST(GITHUB_GET_TOKEN_PATH)
    @Headers("Accept: application/json")
    suspend fun refreshAccessToken(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String = GITHUB_CLIENT_ID,
        @Field("grant_type") grantType: String = "refresh_token"
    ): AuthTokenDTO

}