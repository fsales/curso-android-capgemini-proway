package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate
import javax.annotation.concurrent.Immutable

// =============================================================================
// ItemExtrato — modelo de domínio para o extrato unificado de ganhos e gastos.
// Sealed class com propriedades abstratas comuns para ordenação e exibição.
// =============================================================================
@Immutable
sealed class ItemExtrato {
    abstract val id: Long
    abstract val descricao: String
    abstract val valor: BigDecimal
    abstract val data: LocalDate

    @Immutable
    data class GanhoItem(val ganho: Ganho) : ItemExtrato() {
        override val id: Long = ganho.id
        override val descricao: String = ganho.descricao
        override val valor: BigDecimal = ganho.valor
        override val data: LocalDate = ganho.dataRecebimento
    }

    @Immutable
    data class GastoItem(val gasto: Gasto) : ItemExtrato() {
        override val id: Long = gasto.id
        override val descricao: String = gasto.descricao
        override val valor: BigDecimal = gasto.valor
        override val data: LocalDate = gasto.dataGasto
    }
}
