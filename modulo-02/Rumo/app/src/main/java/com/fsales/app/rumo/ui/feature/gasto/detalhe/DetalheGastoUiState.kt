package com.fsales.app.rumo.ui.feature.gasto.detalhe

import com.fsales.app.rumo.core.domain.model.Gasto

data class DetalheGastoUiState(
    val gasto: Gasto? = null,
    val carregando: Boolean = true,
    val erro: String? = null,
    val dialogConfirmacao: TipoConfirmacaoGasto? = null,
    val excluindo: Boolean = false,
)
