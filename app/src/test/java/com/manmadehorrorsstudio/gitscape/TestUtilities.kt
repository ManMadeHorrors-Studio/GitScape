package com.manmadehorrorsstudio.gitscape

import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.unmockkStatic

object TestUtils {

    const val MOCK_AUTH_DEVICE_CODE_DURATION_SECONDS = 120L
    const val MOCK_ACCESS_TOKEN_DURATION_SECONDS = 7200L
    const val MOCK_REFRESH_TOKEN_DURATION_SECONDS = 172800L

    val mockedUser = GitHubUser(
        id = "666",
        userName = "Man Made Horrors Studio",
        avatarUrl = "https://avatars.githubusercontent.com/u/263767797?v=4",
        homePageUrl = null
    )

    val mockedException = Exception("Errore di rete")

    private const val UTILITIES_CLASS =
        "com.manmadehorrorsstudio.gitscape.UtilitiesKt"

    fun mockGitScapeLog() {
        mockkStatic(UTILITIES_CLASS)

        every {
            gitScapeLog(any<String>())
        } just Runs

        every {
            gitScapeLog(
                any<String>(),
                any<Exception>()
            )
        } just Runs
    }

    fun unmockGitScapeLog() {
        unmockkStatic(UTILITIES_CLASS)
    }
}