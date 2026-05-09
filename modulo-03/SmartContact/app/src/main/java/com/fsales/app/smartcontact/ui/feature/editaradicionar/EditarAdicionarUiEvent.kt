package com.fsales.app.smartcontact.ui.feature.editaradicionar

sealed interface EditarAdicionarUiEvent {
    data object NavigateBack  : EditarAdicionarUiEvent
    data object ErroAoSalvar  : EditarAdicionarUiEvent
}

