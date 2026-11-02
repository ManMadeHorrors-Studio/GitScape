package com.manmadehorrorsstudio.gitscape.ui.models

import ai.koog.prompt.llm.GoogleLLMProvider
import ai.koog.prompt.llm.OpenAILLMProvider
import com.manmadehorrorsstudio.gitscape.R

enum class LlmLogos (val id: String, val logo: Int) {
    OPENAI_LOGO(OpenAILLMProvider.id , R.drawable.ai_openai_logo),
    GEMINI_LOGO(GoogleLLMProvider.id, R.drawable.ai_gemini_logo);
}