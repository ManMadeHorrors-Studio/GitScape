package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.models.GitHubRepoDTO
import com.manmadehorrorsstudio.gitscape.data.models.GitHubUserDTO
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubRepo
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GitHubMapperTest {

    @Test
    fun `GitHubRepoDTO maps correctly to GitHubRepo`() {

        val dto = GitHubRepoDTO(
            id = "123",
            name = "GitScape",
            language = "Kotlin",
            stargazersCount = 42,
            htmlUrl = "https://github.com/example/gitscape"
        )

        val result = dto.mapToDomain()

        assertEquals(
            GitHubRepo(
                id = "123",
                name = "GitScape",
                primaryLanguage = "Kotlin",
                stars = 42,
                htmlUrl = "https://github.com/example/gitscape",
                description = null,
                ownerLogin = null
            ),
            result
        )
    }

    @Test
    fun `GitHubRepoDTO maps null stars to zero`() {

        val dto = GitHubRepoDTO(
            id = "123",
            name = "GitScape",
            language = "Kotlin",
            stargazersCount = null,
            htmlUrl = "https://github.com/example/gitscape"
        )

        val result = dto.mapToDomain()

        assertEquals(0, result.stars)
    }

    @Test
    fun `GitHubRepoDTO maps null id and name to empty defaults`() {

        val dto = GitHubRepoDTO(
            id = null,
            name = null,
            language = null,
            stargazersCount = null,
            htmlUrl = null,
            description = null
        )

        val result = dto.mapToDomain()

        assertEquals("", result.id)
        assertEquals("", result.name)
        assertNull(result.primaryLanguage)
        assertEquals(0, result.stars)
        assertNull(result.htmlUrl)
    }

    @Test
    fun `GitHubUserDTO maps correctly to GitHubUser`() {

        val dto = GitHubUserDTO(
            id = "123",
            name = "Man Made Horrors Studio",
            avatarUrl = "https://github.com/avatar.jpg"
        )

        val result = dto.mapToDomain()

        assertEquals(
            GitHubUser(
                id = "123",
                userName = "Man Made Horrors Studio",
                avatarUrl = "https://github.com/avatar.jpg",
                homePageUrl = null
            ),
            result
        )
    }

    @Test
    fun `GitHubUserDTO maps null id and name to empty defaults`() {

        val dto = GitHubUserDTO(
            id = null,
            name = null,
            avatarUrl = null
        )

        val result = dto.mapToDomain()

        assertEquals("", result.id)
        assertEquals("", result.userName)
        assertNull(result.avatarUrl)
    }
}