package com.manmadehorrorsstudio.gitscape.data.ai

data class LlmException(
    val errorInt: Int
) : Throwable()