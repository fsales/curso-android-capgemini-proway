package com.fsales.app.rumo.ui

import androidx.annotation.StringRes

sealed interface UiEvent {
    data class ShowSnackbar(@param:StringRes val resId: Int) : GanhoUiEvent
}

sealed interface GanhoUiEvent : UiEvent {
    data object NavigateToCadastro : GanhoUiEvent
}