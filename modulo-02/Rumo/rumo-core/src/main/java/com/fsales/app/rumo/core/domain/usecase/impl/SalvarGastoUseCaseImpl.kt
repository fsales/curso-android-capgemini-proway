package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import java.math.BigDecimal
import javax.inject.Inject

class SalvarGastoUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository
) : SalvarGastoUseCase {

    override suspend fun invoke(gasto: Gasto): Result<Long> {
        validar(gasto)?.let { return Result.failure(IllegalArgumentException(it)) }
        return gastoRepository.salvar(gasto)
    }

    private fun validar(gasto: Gasto): String? {
        if (gasto.descricao.isBlank()) return "A descrição do gasto é obrigatória."
        if (gasto.valor <= BigDecimal.ZERO) return "O valor do gasto deve ser maior que zero."

        return runCatching {
            validarCompetencia(gasto.mesReferencia, gasto.anoReferencia)
            require(gasto.dataGasto.monthValue == gasto.mesReferencia) {
                "O mês de referência deve corresponder à data do gasto."
            }
            require(gasto.dataGasto.year == gasto.anoReferencia) {
                "O ano de referência deve corresponder à data do gasto."
            }
        }.exceptionOrNull()?.message
    }
}
