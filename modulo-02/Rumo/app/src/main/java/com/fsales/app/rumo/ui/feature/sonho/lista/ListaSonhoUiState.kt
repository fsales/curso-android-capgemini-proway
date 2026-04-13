package com.fsales.app.rumo.ui.feature.sonho.lista

import com.fsales.app.rumo.core.domain.model.ProjecaoSonho

data class ListaSonhoUiState(
    val projecoes: List<ProjecaoSonho> = emptyList(),
    val carregando: Boolean = false,
    val erro: String? = null,
)

