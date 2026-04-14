package com.fsales.app.rumo.ui.feature.ganho.detalhe

import com.fsales.app.rumo.core.domain.model.Ganho

data class DetalheGanhoUiState(
    val ganho: Ganho? = null,
    val carregando: Boolean = true,
    val erro: String? = null,
    val dialogConfirmacao: TipoConfirmacaoGanho? = null,
    val excluindo: Boolean = false,
)
