package com.fsales.app.rumo.ui.feature.sonho.lista

import com.fsales.app.rumo.core.domain.model.Sonho

data class ListaSonhoUiState(
    val sonhos: List<Sonho> = emptyList(),
    val carregando: Boolean = false,
    val erro: String? = null,
)

