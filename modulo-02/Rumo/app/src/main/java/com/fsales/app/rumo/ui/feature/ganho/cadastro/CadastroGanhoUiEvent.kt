package com.fsales.app.rumo.ui.feature.ganho.cadastro

sealed interface CadastroGanhoUiEvent {
    data object NavigateBack : CadastroGanhoUiEvent
    data object ErroAoSalvar : CadastroGanhoUiEvent  // infra error → Screen mostra Snackbar com string local
}
