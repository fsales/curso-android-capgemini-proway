package com.fsales.app.rumo.ui.feature.saldo

import java.math.BigDecimal
import java.time.YearMonth

data class SaldoUiState(
    val mesAno: YearMonth = YearMonth.now(),
    val totalGanhos: BigDecimal? = null,
    val totalGastos: BigDecimal? = null,
    val saldo: BigDecimal? = null,
    val carregando: Boolean = false,
    val erro: String? = null,
)

