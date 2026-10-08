package com.manmadehorrorsstudio.gitscape.ui.models

sealed interface UIEvents {
    data class ShowError(val message: String?) : UIEvents
    data class PontToRepo(val id: String) : UIEvents
}