package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.addCurrentTime
import com.manmadehorrorsstudio.gitscape.isNotExpired
import com.manmadehorrorsstudio.gitscape.data.local.AuthTokensEntity
import com.manmadehorrorsstudio.gitscape.data.models.AuthDeviceCodeDTO
import com.manmadehorrorsstudio.gitscape.data.models.AuthTokenDTO
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AuthMapperTest {
    @Test
    fun `device dto maps values to epoch seconds`() {
        val result = AuthDeviceCodeDTO("device", "user", "https://verify", 60L, 5).mapToDomain()

        assertEquals("device", result.deviceCode)
        assertEquals("user", result.userCode)
        assertEquals("https://verify", result.verificationUri)
        assertEquals(5, result.interval)
        assertTrue(result.expiresAt.isNotExpired())
    }

    @Test
    fun `device dto maps nullable values to documented defaults`() {
        val result = AuthDeviceCodeDTO().mapToDomain()

        assertEquals("", result.deviceCode)
        assertEquals("", result.userCode)
        assertEquals("", result.verificationUri)
        assertEquals(-1, result.expiresAt)
        assertEquals(-1, result.interval)
    }

    @Test
    fun `token dto converts expiration durations to epoch seconds`() {
        val result = AuthTokenDTO("access", "refresh", 10L, 20L).mapToDomain("id")

        assertEquals("id", result.githubUserId)
        assertEquals("access", result.accessToken)
        assertEquals("refresh", result.refreshToken)
        assertTrue(result.accessTokenExpiresAt.isNotExpired())
        assertTrue(result.refreshTokenExpiresAt.isNotExpired())
    }

    @Test
    fun `token dto maps nullable token values to empty strings and nullable expirations`() {
        val result = AuthTokenDTO().mapToDomain("id")

        assertEquals(AuthTokens("id", "", "", null, null), result)
    }

    @Test
    fun `entity and domain token mappings preserve epoch expiration values`() {
        val tokens = AuthTokens("id", "access", "refresh", 100L, 200L)

        assertEquals(tokens, tokens.toEntity().mapToDomain())
        assertEquals(tokens, AuthTokensEntity("id", "access", "refresh", 100L, 200L).mapToDomain())
    }
}
