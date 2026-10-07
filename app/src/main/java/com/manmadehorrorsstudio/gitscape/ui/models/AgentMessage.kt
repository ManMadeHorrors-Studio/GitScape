package com.manmadehorrorsstudio.gitscape.ui.models

import ai.koog.prompt.message.Message.Role

data class AgentMessage(
    val role: Role,
    val text: String
)