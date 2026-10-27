package com.manmadehorrorsstudio.gitscape.data.mappers

import com.manmadehorrorsstudio.gitscape.data.local.AuthTokensEntity
import com.manmadehorrorsstudio.gitscape.data.models.AuthDeviceCodeDTO
import com.manmadehorrorsstudio.gitscape.data.models.AuthTokenDTO
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthMapperTest {
    @Test
    fun `device dto maps values and expiration`() {
        val result = AuthDeviceCodeDTO("device", "user", "https://verify", 60, 5).mapToDomain()
        assertEquals("device", result.deviceCode)
        assertEquals("user", result.userCode)
        assertEquals("https://verify", result.verificationUri)
        assertEquals(5, result.interval)
        assertEquals(true, result.expiresAt > System.currentTimeMillis())
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
    fun `token dto and entity mappings preserve values`() {
        val dto = AuthTokenDTO("access", "refresh", 10, 20)
        assertEquals(AuthTokens("id", "access", "refresh", 10, 20), dto.mapToDomain("id"))
        val tokens = AuthTokens("id", "access", "refresh", 10, 20)
        assertEquals(tokens, tokens.toEntity().mapToDomain())
        assertEquals(tokens, AuthTokensEntity("id", "access", "refresh", 10, 20).mapToDomain())
    }
}
