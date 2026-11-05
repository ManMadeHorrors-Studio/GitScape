package com.manmadehorrorsstudio.gitscape.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.data.local.LlmKoogModels.Companion.getAllLlmKoogModels
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration
import com.manmadehorrorsstudio.gitscape.domain.models.LlmConfiguration.Companion.getLlmConfiguration
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalLlmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LlmConfigurationViewModel @Inject constructor(
    private val localLlmRepository: LocalLlmRepository
): ViewModel() {

    private val _providerSelected = MutableStateFlow<String?> (null)

    fun getLlmConfigurationsState() = localLlmRepository.getCachedLlmConfigurations()

    val selectedLlmConfigurationState: StateFlow<LlmConfiguration?> =
        combine(
            _providerSelected,
            getLlmConfigurationsState()
        ) { provider, configurations ->
            configurations.firstOrNull {
                it.llmProvider.id == provider
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun setLlmInConfiguration(id: String) {
        _providerSelected.value = id
    }

    fun getModelsForConfiguration() =
        getAllLlmKoogModels(_providerSelected.value).let { models ->
            val selectedModelId = selectedLlmConfigurationState.value?.selectedModel
            val selectedModel = models.firstOrNull { model ->
                model.id == selectedModelId
            }

            selectedModel?.let {
                listOf(selectedModel) + models.filterNot { model ->
                    model.id == selectedModel.id
                }
            } ?: models
        }

    fun saveAiKey(key: String?) {
        selectedLlmConfigurationState.value?.let { slcs ->
            saveConfiguration(
                llmConfiguration = getLlmConfiguration(
                    providerId = slcs.llmProvider.id,
                    aiKey = key,
                    selectedModel = slcs.selectedModel,
                    selected = slcs.selected
                )
            )
        }
    }

    fun saveModel(model: String) {
        selectedLlmConfigurationState.value?.let { slcs ->
            val selectedModel = getLlmConfiguration(
                providerId = slcs.llmProvider.id,
                aiKey = slcs.aiKey,
                selectedModel = model,
                selected = true
            )

            getLlmConfigurationsState().value.forEach { conf ->
                saveConfiguration(
                    llmConfiguration =
                        if (conf.llmProvider.id == selectedModel?.llmProvider?.id)
                            selectedModel
                        else
                            getLlmConfiguration(
                                providerId = conf.llmProvider.id,
                                aiKey = conf.aiKey,
                                selectedModel = conf.selectedModel,
                                selected = false
                            )
                )
            }
        }
    }

    private fun saveConfiguration(llmConfiguration: LlmConfiguration?) {
        llmConfiguration?.let { conf ->
            viewModelScope.launch {
                localLlmRepository.saveLlmConfiguration(conf)
            }
        }
    }
}