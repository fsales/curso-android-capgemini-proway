package com.fsales.app.rumo.ui.feature.gasto.cadastro

sealed interface CadastroGastoUiEvent {
    data object NavigateBack : CadastroGastoUiEvent
    data object ErroAoSalvar : CadastroGastoUiEvent
}

