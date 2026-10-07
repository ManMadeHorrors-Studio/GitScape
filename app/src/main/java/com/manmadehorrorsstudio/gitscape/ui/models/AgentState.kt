package com.manmadehorrorsstudio.gitscape.ui.models

import ai.koog.prompt.message.Message
import ai.koog.prompt.message.MessagePart

data class AgentState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

fun Message.toText(): String =
    parts
        .filterIsInstance<MessagePart.Text>()
        .joinToString("\n") { it.text }