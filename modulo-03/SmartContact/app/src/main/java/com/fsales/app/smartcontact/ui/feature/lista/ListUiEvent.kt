package com.fsales.app.smartcontact.ui.feature.lista

sealed interface ListUiEvent {
    data object NavegaParaNovo                 : ListUiEvent
    data class  NavegaParaEdicao(val id: Long) : ListUiEvent
    data object ExcluirSucesso                 : ListUiEvent
    data object ErroAoExcluir                 : ListUiEvent
}
