package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class Sonho(
    val id: Long = 0L,
    val titulo: String,
    val descricao: String? = null,
    val valorMeta: BigDecimal,
    val valorAtual: BigDecimal = BigDecimal.ZERO,
    val prioridade: PrioridadeSonho = PrioridadeSonho.MEDIA,
    val prazoAlvo: LocalDate? = null,
    val status: StatusSonho = StatusSonho.NAO_INICIADO,
    val dataCriacao: Instant = Instant.now(),
    val dataConclusao: Instant? = null
)
