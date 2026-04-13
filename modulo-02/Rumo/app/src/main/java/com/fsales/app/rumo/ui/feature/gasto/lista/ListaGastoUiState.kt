package com.fsales.app.rumo.ui.feature.gasto.lista

import com.fsales.app.rumo.core.domain.model.Gasto
import java.time.YearMonth

data class ListaGastoUiState(
    val gastos: List<Gasto> = emptyList(),
    val mesAno: YearMonth = YearMonth.now(),
    val carregando: Boolean = false,
    val erro: String? = null,
)

