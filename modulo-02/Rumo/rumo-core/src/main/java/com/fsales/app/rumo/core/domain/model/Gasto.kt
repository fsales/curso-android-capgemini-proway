package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Gasto(
    val id: Long = 0L,
    val descricao: String,
    val valor: BigDecimal,
    val dataGasto: LocalDate,
    val mesReferencia: Int,
    val anoReferencia: Int,
    val categoria: CategoriaGasto,
    val essencial: Boolean = false,
    val recorrente: Boolean = false,
    val observacao: String? = null
)
