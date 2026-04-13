package com.fsales.app.rumo.ui.feature.sonho.cadastro

sealed interface CadastroSonhoUiEvent {
    data object NavigateBack : CadastroSonhoUiEvent
    data object ErroAoSalvar : CadastroSonhoUiEvent
}

