package com.fsales.app.rumo.ui.feature.sonho.detalhe

import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import com.fsales.app.rumo.core.domain.model.Sonho

data class DetalheSonhoUiState(
    val sonho: Sonho? = null,
    val projecao: ProjecaoSonho? = null,
    val carregando: Boolean = true,
    val erro: String? = null,
    val exibirDialogoConclusao: Boolean = false,
)

