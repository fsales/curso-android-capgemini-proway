package com.fsales.app.smartcontact.ui.feature.lista

sealed interface ListEvent {
    data object NavegaParaNovo                    : ListEvent
    data class  NavegaParaEdicao(val id: Long)    : ListEvent
}

