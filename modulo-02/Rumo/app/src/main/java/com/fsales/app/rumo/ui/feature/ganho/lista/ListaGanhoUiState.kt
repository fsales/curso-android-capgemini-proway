package com.fsales.app.rumo.ui.feature.ganho.lista

import com.fsales.app.rumo.core.domain.model.Ganho
import java.time.YearMonth

data class ListaGanhoUiState(
    val ganhos: List<Ganho> = emptyList(),
    val mesAno: YearMonth = YearMonth.now(),
    val carregando: Boolean = false,
    val erro: String? = null,
)

