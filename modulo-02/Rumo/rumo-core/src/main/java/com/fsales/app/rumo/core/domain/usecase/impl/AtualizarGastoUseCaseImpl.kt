package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.model.GastoErro
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.AtualizarGastoUseCase
import com.fsales.app.rumo.core.domain.usecase.GastoErroException
import java.math.BigDecimal
import javax.inject.Inject

class AtualizarGastoUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository,
) : AtualizarGastoUseCase {

    override suspend operator fun invoke(gasto: Gasto): Result<Unit> {
        validar(gasto)?.let { return Result.failure(GastoErroException(it)) }
        return gastoRepository.atualizar(gasto)
    }

    private fun validar(gasto: Gasto): GastoErro? {
        if (gasto.descricao.isBlank()) return GastoErro.DescricaoObrigatoria
        if (gasto.valor <= BigDecimal.ZERO) return GastoErro.ValorInvalido
        if (gasto.dataVencimento != null && gasto.dataVencimento < gasto.dataGasto) {
            return GastoErro.DataVencimentoInvalida
        }
        return null
    }
}
