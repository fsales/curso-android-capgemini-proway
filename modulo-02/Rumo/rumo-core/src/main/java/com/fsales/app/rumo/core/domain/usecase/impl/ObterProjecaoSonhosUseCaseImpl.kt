package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import com.fsales.app.rumo.core.domain.model.StatusSonho
import com.fsales.app.rumo.core.domain.model.calcularProjecao
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import com.fsales.app.rumo.core.domain.usecase.ObterProjecaoSonhosUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterSaldoMensalUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ObterProjecaoSonhosUseCaseImpl @Inject constructor(
    private val sonhoRepository: SonhoRepository,
    private val obterSaldoMensal: ObterSaldoMensalUseCase,
) : ObterProjecaoSonhosUseCase {

    override fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<ProjecaoSonho>> = flow {
        // validarCompetencia dentro do flow — exceção propagada corretamente via .catch na ViewModel
        validarCompetencia(mesReferencia, anoReferencia)

        combine(
            sonhoRepository.listarTodos(),
            obterSaldoMensal(mesReferencia, anoReferencia),
        ) { sonhos, saldo ->
            // Apenas sonhos não concluídos participam da projeção de alocação de saldo
            val ativos    = sonhos.filter { it.status != StatusSonho.CONCLUIDO }
            val pesoTotal = ativos.sumOf { it.prioridade.peso }

            ativos.map { sonho ->
                val saldoAlocado = if (pesoTotal > 0 && saldo.saldo > BigDecimal.ZERO) {
                    saldo.saldo
                        .multiply(BigDecimal(sonho.prioridade.peso))
                        .divide(BigDecimal(pesoTotal), 2, RoundingMode.HALF_UP)
                } else {
                    BigDecimal.ZERO
                }
                sonho.calcularProjecao(saldoAlocado)
            }
        }.collect { emit(it) }
    }
}
