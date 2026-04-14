package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Ganho(
    val id: Long = 0L,
    val descricao: String,
    val valor: BigDecimal,
    val dataRecebimento: LocalDate,
    val mesReferencia: Int,
    val anoReferencia: Int,
    val tipo: TipoGanho,
    val recorrente: Boolean = false,
    val observacao: String? = null,
    val grupoRecorrenciaId: String? = null,
)
