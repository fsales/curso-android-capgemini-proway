package com.fsales.app.rumo.ui.feature.extrato

import com.fsales.app.rumo.core.domain.model.ItemExtrato
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class ExtratoUiState(
    val itensPorData: Map<LocalDate, List<ItemExtrato>> = emptyMap(),
    val totalGanhos: BigDecimal = BigDecimal.ZERO,
    val totalGastos: BigDecimal = BigDecimal.ZERO,
    val saldoPeriodo: BigDecimal = BigDecimal.ZERO,
    val mesAno: YearMonth = YearMonth.now(),
    val carregando: Boolean = false,
    val erro: String? = null,
)
