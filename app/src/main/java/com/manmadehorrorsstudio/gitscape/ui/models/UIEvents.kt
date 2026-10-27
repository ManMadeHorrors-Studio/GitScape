package com.manmadehorrorsstudio.gitscape.ui.models

sealed interface UIEvents {
    data class ShowError(
        val message: Int?,
        val onAlertConfirm: (() -> Unit)? = null
    ) : UIEvents

    data class PointToRepo(val id: String) : UIEvents
    data class CallOpenBrowser(val url: String?, val stringToCopy: String?) : UIEvents
    data object CallRefreshRepo : UIEvents
}