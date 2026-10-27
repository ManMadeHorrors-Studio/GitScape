package com.manmadehorrorsstudio.gitscape.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.data.local.AuthCache
import com.manmadehorrorsstudio.gitscape.domain.models.AuthTokens
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalGitHubRepository
import com.manmadehorrorsstudio.gitscape.domain.repositories.LocalTokensRepository
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetAuthDeviceUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetAuthTokensUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.GetGitHubUserUseCase
import com.manmadehorrorsstudio.gitscape.domain.usecases.RefreshAuthTokensUseCase
import com.manmadehorrorsstudio.gitscape.gitScapeLog
import com.manmadehorrorsstudio.gitscape.ui.models.AuthError
import com.manmadehorrorsstudio.gitscape.ui.models.AuthState
import com.manmadehorrorsstudio.gitscape.ui.models.AuthStatus
import com.manmadehorrorsstudio.gitscape.ui.models.UIEvents
import com.manmadehorrorsstudio.gitscape.ui.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.onSuccess

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthDeviceUseCase: GetAuthDeviceUseCase,
    private val getAuthTokensUseCase: GetAuthTokensUseCase,
    private val getGitHubUserUseCase: GetGitHubUserUseCase,
    private val refreshAuthTokensUseCase: RefreshAuthTokensUseCase,
    private val localTokensRepository: LocalTokensRepository,
    private val settingsRepository: SettingsRepository,
    private val localGitHubRepository: LocalGitHubRepository,
    private val authCache: AuthCache
) : ViewModel() {

    private val _authState =
        MutableStateFlow<AuthState>(AuthState())

    val authState: StateFlow<AuthState> =
        _authState.asStateFlow()

    private val _authEvents = Channel<UIEvents>(Channel.CONFLATED)
    val authEvents = _authEvents.receiveAsFlow()

    private var authTokensJob: Job? = null

    init {
        collectAccessToken()
    }

    fun advanceStep() {
        when ( _authState.value.authStatus) {
            AuthStatus.STEP1 -> {
                startLoading()
                callAuthDevice()
            }
            AuthStatus.STEP2 -> {
                checkAuthDeviceCodeNotExpiredThenCallAuthTokens()
            }
            AuthStatus.BROWSING -> {
                authTokensJob?.cancel()
                clearState()
            }
            AuthStatus.SUCCESS -> {
                _authState.update { authS ->
                    authS.copy(
                        authStatus = AuthStatus.VALIDATED
                    )
                }
                viewModelScope.launch {
                    _authEvents.send(
                        UIEvents.CallRefreshRepo
                    )
                }
            }
            AuthStatus.VALIDATED -> {
                //Do nothing
            }
            AuthStatus.OFFLINE -> {
                //Do nothing
            }
        }
    }

    fun collectAccessToken() {
        viewModelScope.launch {
            localTokensRepository.getTokens()?.let { localTokens ->
                if(localTokens.accessTokenExpiresAt.isNotExpired()) {
                    loadUserAndTestAccessToken(localTokens, true)
                } else {
                    useRefreshToken(localTokens)
                }
            } ?: run {
                setNoUserRegistered()
                _authState.update { authS ->
                    authS.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun startLoading() {
        _authState.update { authS ->
            authS.copy(
                isLoading = true
            )
        }
    }

    private suspend fun useRefreshToken(localTokens: AuthTokens) {
        if(localTokens.refreshTokenExpiresAt.isNotExpired()) {
            runCatching {refreshAuthTokensUseCase(localTokens.refreshToken)}
                .onSuccess { authTokens ->
                    gitScapeLog("callAuthTokens result: $authTokens")
                    loadUserAndTestAccessToken(authTokens, false)
                }
                .onFailure { exception ->
                    gitScapeLog("callAuthTokens error", exception)

                    if (exception is HttpException) {
                        val errorBody = exception.response()
                            ?.errorBody()
                            ?.string()
                        if (errorBody == AuthError.INVALID_REFRESH_TOKEN.type) {
                            setNoUserRegistered()
                            clearState()
                            _authEvents.send(
                                UIEvents.ShowError(
                                    message = AuthError.INVALID_REFRESH_TOKEN.message
                                )
                            )
                        } else
                            offlineMode()
                    }
                }
        } else {
            setNoUserRegistered()
            clearState()
            _authEvents.send(
                UIEvents.ShowError(
                    message = AuthError.INVALID_REFRESH_TOKEN.message
                )
            )
        }
    }

    private fun setNoUserRegistered() {
        viewModelScope.launch {
            settingsRepository.clearRepoHash()
            localGitHubRepository.clearGitHubRepositories()
            authCache.clearCache()
            localTokensRepository.deleteTokens()
        }
    }

    private fun clearState() {
        authCache.clearCache()
        _authState.update { authS ->
            authS.copy(
                authDeviceCode = null,
                authStatus = AuthStatus.STEP1,
                isLoading = false
            )
        }
    }

    private suspend fun loadUserAndTestAccessToken(authTokens: AuthTokens, tokensFromLocal: Boolean) {
        authCache.updateAccessToken(authTokens.accessToken)
        runCatching { getGitHubUserUseCase() }
            .onSuccess { user ->
                gitScapeLog("loadUser result: $user")
                _authState.update { authS ->
                    authS.copy(
                        gitHubUser = user
                    )
                }

                if (tokensFromLocal) {
                    if (localTokensRepository.getTokens()?.githubUserId != user.id) {
                        setNoUserRegistered()
                        clearState()
                        _authEvents.send(
                            UIEvents.ShowError(
                                message = AuthError.DIFFERENT_USER.message
                            )
                        )
                    } else {
                        _authState.update { authS ->
                            authS.copy(
                                authDeviceCode = null,
                                authStatus = AuthStatus.VALIDATED,
                                isLoading = false
                            )
                        }
                        _authEvents.send(
                            UIEvents.CallRefreshRepo
                        )
                    }
                } else {

                    val authTokensWithUserId = AuthTokens(
                        githubUserId = user.id,
                        accessToken = authTokens.accessToken,
                        refreshToken = authTokens.refreshToken,
                        accessTokenExpiresAt = authTokens.accessTokenExpiresAt,
                        refreshTokenExpiresAt = authTokens.refreshTokenExpiresAt
                    )

                    localTokensRepository.saveTokens(authTokensWithUserId)

                    _authState.update { authS ->
                        authS.copy(
                            authDeviceCode = null,
                            authStatus = AuthStatus.SUCCESS
                        )
                    }
                }
            }
            .onFailure { exception ->
                gitScapeLog("loadUser error", exception as? Exception)
                clearState()
                if (exception is HttpException && exception.code() == 401) {
                    setNoUserRegistered()
                    _authEvents.send(
                        UIEvents.ShowError(
                            message = AuthError.INVALID_ACCESS_TOKEN.message
                        )
                    )
                } else
                    offlineMode()
            }
    }

    private suspend fun offlineMode() {
        _authState.update { authS ->
            authS.copy(
                authDeviceCode = null,
                authStatus = AuthStatus.OFFLINE,
                isLoading = false
            )
        }

        _authEvents.send(
            UIEvents.CallRefreshRepo
        )
    }

    private fun callAuthDevice() {
        viewModelScope.launch {
            runCatching { getAuthDeviceUseCase() }
                .onSuccess { authDeviceCode ->
                    gitScapeLog("callAuthDevice result: $authDeviceCode")

                    _authState.update { authS ->
                        authS.copy(
                            authDeviceCode = authDeviceCode,
                            isLoading = false,
                            authStatus = AuthStatus.STEP2
                        )
                    }
                }
                .onFailure { exception ->
                    gitScapeLog("callAuthDevice error", exception as? Exception)

                }
        }
    }

    private suspend fun callAuthTokens() {
        _authState.value.authDeviceCode?.let { authDevice ->
            runCatching {
                getAuthTokensUseCase(
                    deviceCode = authDevice.deviceCode,
                    deviceCodeExpiresAt = authDevice.expiresAt,
                    initialInterval = authDevice.interval
                )
            }
                .onSuccess { authTokens ->
                    gitScapeLog("callAuthTokens result: $authTokens")
                    loadUserAndTestAccessToken(authTokens, false)
                }
                .onFailure { exception ->
                    gitScapeLog("callAuthTokens error", exception as? Exception)

                    if (exception !is CancellationException) {
                        exception.message?.toIntOrNull()?.let {
                            _authEvents.send(
                                UIEvents.ShowError(
                                    message = exception.message?.toIntOrNull() ?: R.string.AUTH_ERROR_GENERIC
                                )
                            )
                        }
                    }
                    clearState()
                }
        }
    }

    private fun checkAuthDeviceCodeNotExpiredThenCallAuthTokens() {
        authTokensJob = viewModelScope.launch {
            _authState.value.authDeviceCode?.expiresAt?.let { expiresAt ->
                if (expiresAt < System.currentTimeMillis()) {
                    clearState()
                    _authEvents.send(
                        UIEvents.ShowError(
                            message = R.string.AUTH_WELCOME_TIMEOUT
                        )
                    )
                } else {
                    waitForBrowser()
                    callAuthTokens()
                }
            }
        }
    }

    private suspend fun waitForBrowser() {
        _authState.update { authS ->
            authS.copy(
                authStatus = AuthStatus.BROWSING
            )
        }

        _authEvents.send(
            UIEvents.CallOpenBrowser(
                url = _authState.value.authDeviceCode?.verificationUri,
                stringToCopy = _authState.value.authDeviceCode?.userCode
            )
        )
    }

    private fun Long?.isNotExpired(): Boolean {
        return this?.let {
            it < System.currentTimeMillis()
        } == true
    }
}