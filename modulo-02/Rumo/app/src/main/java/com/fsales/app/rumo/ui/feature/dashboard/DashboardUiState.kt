package com.fsales.app.rumo.ui.feature.dashboard

import java.math.BigDecimal
import java.time.YearMonth

data class DashboardUiState(
    val mesAno: YearMonth = YearMonth.now(),
    val totalGanhos: BigDecimal? = null,
    val totalGastos: BigDecimal? = null,
    val saldo: BigDecimal? = null,
    val carregando: Boolean = false,
    val erro: String? = null,
)
