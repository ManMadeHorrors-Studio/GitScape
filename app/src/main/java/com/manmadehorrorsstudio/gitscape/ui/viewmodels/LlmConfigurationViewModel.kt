package com.manmadehorrorsstudio.gitscape.ui.viewmodels


import androidx.lifecycle.ViewModel
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalLlmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LlmConfigurationViewModel @Inject constructor(
    private val localLlmRepository: LocalLlmRepository
): ViewModel() {


    fun getCachedLlmConfiguration() = localLlmRepository.getCachedLlmConfigurations()


    fun setLlmInConfiguration(llmConfiguration: LlmConfiguration?) {


    }
}