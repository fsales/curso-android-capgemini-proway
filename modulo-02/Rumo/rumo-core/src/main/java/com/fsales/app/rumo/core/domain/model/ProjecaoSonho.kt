package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ProjecaoSonho(
    val sonho: Sonho,
    val valorRestante: BigDecimal,
    val percentualConcluido: BigDecimal,
    val mesesNecessarios: Int?,
    val seraAlcancadoNoPrazo: Boolean
)

fun Sonho.calcularProjecao(saldoMensal: BigDecimal): ProjecaoSonho {
    val valorRestante = valorMeta.subtract(valorAtual).max(BigDecimal.ZERO)

    val percentualConcluido = if (valorMeta > BigDecimal.ZERO) {
        valorAtual
            .divide(valorMeta, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal(100))
            .setScale(2, RoundingMode.HALF_UP)
    } else {
        BigDecimal.ZERO
    }

    val mesesNecessarios = if (saldoMensal > BigDecimal.ZERO && valorRestante > BigDecimal.ZERO) {
        valorRestante.divide(saldoMensal, 0, RoundingMode.CEILING).toInt()
    } else {
        null
    }

    val seraAlcancadoNoPrazo = when {
        valorRestante <= BigDecimal.ZERO -> true
        mesesNecessarios == null -> false
        prazoAlvo == null -> true
        else -> {
            val mesesAtePrazo = ChronoUnit.MONTHS.between(
                LocalDate.now().withDayOfMonth(1),
                prazoAlvo.withDayOfMonth(1)
            ).toInt()
            mesesNecessarios <= mesesAtePrazo
        }
    }

    return ProjecaoSonho(
        sonho = this,
        valorRestante = valorRestante,
        percentualConcluido = percentualConcluido,
        mesesNecessarios = mesesNecessarios,
        seraAlcancadoNoPrazo = seraAlcancadoNoPrazo
    )
}
