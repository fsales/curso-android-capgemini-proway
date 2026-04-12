package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.SaldoMensal
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.ObterSaldoMensalUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import javax.inject.Inject

class ObterSaldoMensalUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository,
    private val gastoRepository: GastoRepository
) : ObterSaldoMensalUseCase {

    override fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<SaldoMensal> {
        validarCompetencia(mesReferencia, anoReferencia)

        return combine(
            ganhoRepository.listarPorMes(mesReferencia, anoReferencia),
            gastoRepository.listarPorMes(mesReferencia, anoReferencia)
        ) { ganhos, gastos ->
            val totalGanhos = ganhos.totalizarValores { it.valor }
            val totalGastos = gastos.totalizarValores { it.valor }

            SaldoMensal(
                mesReferencia = mesReferencia,
                anoReferencia = anoReferencia,
                totalGanhos = totalGanhos,
                totalGastos = totalGastos,
                saldo = totalGanhos.subtract(totalGastos)
            )
        }
    }

    private fun <T> List<T>.totalizarValores(selector: (T) -> BigDecimal): BigDecimal =
        fold(BigDecimal.ZERO) { acc, item -> acc + selector(item) }
}
