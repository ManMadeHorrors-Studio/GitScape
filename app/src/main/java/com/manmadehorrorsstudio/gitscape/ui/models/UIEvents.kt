package com.manmadehorrorsstudio.gitscape.ui.models

sealed interface UIEvents {
    data class ShowError(val message: String?) : UIEvents
}