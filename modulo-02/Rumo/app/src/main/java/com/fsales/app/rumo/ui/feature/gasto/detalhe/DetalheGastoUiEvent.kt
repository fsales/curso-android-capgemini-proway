package com.fsales.app.rumo.ui.feature.gasto.detalhe

import com.fsales.app.rumo.ui.feature.gasto.cadastro.ModoEdicaoGasto

sealed interface DetalheGastoUiEvent {
    data object NavigateBack : DetalheGastoUiEvent
    data class NavigateToCadastro(
        val gastoId: Long,
        val modoEdicao: ModoEdicaoGasto,
    ) : DetalheGastoUiEvent
}
