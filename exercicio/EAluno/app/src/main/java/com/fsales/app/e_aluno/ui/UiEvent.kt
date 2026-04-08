package com.fsales.app.e_aluno.ui

import androidx.annotation.StringRes


sealed interface UiEvent {

    data class ShowSnackbar(@param:StringRes val resId: Int) : ListUiEvent
}

sealed interface ListUiEvent : UiEvent {
    data class NavigateToDetalhes(val id: Long) : ListUiEvent
}

sealed interface DetalhesUiEvent : UiEvent {
    data object NavigateBack : DetalhesUiEvent
}
