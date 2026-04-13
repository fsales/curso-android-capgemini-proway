package com.fsales.app.rumo.core.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class ProjecaoSonho(
    val sonho: Sonho,
    val saldoMensal: BigDecimal,
    val mesesNecessarios: Int?,
    /** `null` quando o sonho não tem prazo definido ou quando concluído — indicador de prazo não deve ser exibido. */
    val seraAlcancadoNoPrazo: Boolean?,
)

fun Sonho.calcularProjecao(saldoMensal: BigDecimal): ProjecaoSonho {
    val mesesNecessarios = if (saldoMensal > BigDecimal.ZERO && valorMeta > BigDecimal.ZERO) {
        valorMeta.divide(saldoMensal, 0, RoundingMode.CEILING).toInt()
    } else {
        null
    }

    val seraAlcancadoNoPrazo: Boolean? = when {
        concluido            -> null
        mesesNecessarios == null -> null
        prazoAlvo == null    -> null
        else -> {
            val mesesAtePrazo = ChronoUnit.MONTHS.between(
                LocalDate.now().withDayOfMonth(1),
                prazoAlvo.withDayOfMonth(1)
            ).toInt()
            mesesNecessarios <= mesesAtePrazo
        }
    }

    return ProjecaoSonho(
        sonho                = this,
        saldoMensal          = saldoMensal,
        mesesNecessarios     = mesesNecessarios,
        seraAlcancadoNoPrazo = seraAlcancadoNoPrazo,
    )
}
