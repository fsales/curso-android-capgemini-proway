package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.model.GastoErro
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.GastoErroException
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import java.math.BigDecimal
import javax.inject.Inject

class SalvarGastoUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository
) : SalvarGastoUseCase {

    override suspend fun invoke(gasto: Gasto): Result<Long> {
        validar(gasto)?.let { return Result.failure(GastoErroException(it)) }
        return gastoRepository.salvar(gasto)
    }

    private fun validar(gasto: Gasto): GastoErro? {
        if (gasto.descricao.isBlank()) return GastoErro.DescricaoObrigatoria
        if (gasto.valor <= BigDecimal.ZERO) return GastoErro.ValorInvalido

        return runCatching {
            validarCompetencia(gasto.mesReferencia, gasto.anoReferencia)
            require(gasto.dataGasto.monthValue == gasto.mesReferencia)
            require(gasto.dataGasto.year == gasto.anoReferencia)
        }.exceptionOrNull()?.let { GastoErro.ValorInvalido }
    }
}
