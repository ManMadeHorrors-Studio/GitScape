package com.manmadehorrorsstudio.gitscape.data.remote

import com.manmadehorrorsstudio.gitscape.GITHUB_GET_REPOS
import com.manmadehorrorsstudio.gitscape.GITHUB_GET_USER
import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import retrofit2.http.GET

interface GitHubApis {

    @GET(GITHUB_GET_REPOS)
    suspend fun getRepositories(): List<GitHubRepoDTO>

    @GET(GITHUB_GET_USER)
    suspend fun getUser(): GitHubUserDTO
}