package com.manmadehorrorsstudio.gitscape.ui.models

import ai.koog.prompt.llm.LLMProvider
import com.manmadehorrorsstudio.gitscape.R

enum class LlmLogos (val id: String, val logo: Int) {
    OPENAI_LOGO(LLMProvider.OpenAI.id , R.drawable.ai_openai_logo),
    GEMINI_LOGO(LLMProvider.Google.id, R.drawable.ai_gemini_logo);
}