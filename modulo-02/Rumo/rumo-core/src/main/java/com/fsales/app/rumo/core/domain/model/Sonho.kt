package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class Sonho(
    val id: Long = 0L,
    val titulo: String,
    val descricao: String? = null,
    val valorMeta: BigDecimal,
    val prioridade: PrioridadeSonho = PrioridadeSonho.MEDIA,
    val prazoAlvo: LocalDate? = null,
    val dataCriacao: Instant = Instant.now(),
    val concluido: Boolean = false,
)
