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
    val dataCriacao: Instant = Instant.now(),
) {
    /** Status derivado dos valores — nunca fica inconsistente com o saldo real. */
    val status: StatusSonho
        get() = when {
            valorAtual >= valorMeta && valorMeta > BigDecimal.ZERO -> StatusSonho.CONCLUIDO
            valorAtual > BigDecimal.ZERO                           -> StatusSonho.EM_ANDAMENTO
            else                                                   -> StatusSonho.NAO_INICIADO
        }
}
