package com.manmadehorrorsstudio.gitscape.data.local

import com.manmadehorrorsstudio.gitscape.data.mappers.mapToDomain
import com.manmadehorrorsstudio.gitscape.data.mappers.mapToEntity
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import javax.inject.Inject

class LocalGitHubRepositoryImpl @Inject constructor(
    private val dao: GitHubDao
) : LocalGitHubRepository {

    override suspend fun getGitHubRepositories(): List<GitHubRepo> {
        return dao.getRepositories().map { entity ->
            entity.mapToDomain()
        }
    }

    override suspend fun saveGitHubRepositories(
        repoList: List<GitHubRepo>
    ) {
        val entities = repoList.map { repo ->
            repo.mapToEntity()
        }

        clearGitHubRepositories()

        if (entities.isNotEmpty()) {
            dao.saveRepositories(entities)
        }
    }

    override suspend fun clearGitHubRepositories() {
        dao.clearRepositories()
    }
}