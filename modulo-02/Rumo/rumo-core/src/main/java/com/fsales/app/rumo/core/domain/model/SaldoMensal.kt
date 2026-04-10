package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal

data class SaldoMensal(
    val mesReferencia: Int,
    val anoReferencia: Int,
    val totalGanhos: BigDecimal,
    val totalGastos: BigDecimal,
    val saldo: BigDecimal
)