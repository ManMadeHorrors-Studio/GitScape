package com.manmadehorrorsstudio.gitscape.ui.models

sealed interface UIEvents {
    data class ShowError(
        val message: Int?,
        val onAlertConfirm: (() -> Unit)? = null
    ) : UIEvents
    data class PointToRepo(val id: String) : UIEvents
}