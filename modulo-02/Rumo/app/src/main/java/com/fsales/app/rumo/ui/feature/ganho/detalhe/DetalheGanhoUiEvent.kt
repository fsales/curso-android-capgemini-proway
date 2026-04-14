package com.fsales.app.rumo.ui.feature.ganho.detalhe

import com.fsales.app.rumo.ui.feature.ganho.cadastro.ModoEdicaoGanho

sealed interface DetalheGanhoUiEvent {
    data object NavigateBack : DetalheGanhoUiEvent
    data class NavigateToCadastro(
        val ganhoId: Long,
        val modoEdicao: ModoEdicaoGanho,
    ) : DetalheGanhoUiEvent
}
