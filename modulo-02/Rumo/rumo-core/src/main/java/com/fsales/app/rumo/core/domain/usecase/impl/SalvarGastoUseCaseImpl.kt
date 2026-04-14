package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.model.GastoErro
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.GastoErroException
import com.fsales.app.rumo.core.domain.usecase.SalvarGastoUseCase
import java.math.BigDecimal
import javax.inject.Inject

class SalvarGastoUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository
) : SalvarGastoUseCase {

    override suspend operator fun invoke(gasto: Gasto): Result<Long> {
        validar(gasto)?.let { return Result.failure(GastoErroException(it)) }
        return gastoRepository.salvar(gasto)
    }

    private fun validar(gasto: Gasto): GastoErro? {
        if (gasto.descricao.isBlank()) return GastoErro.DescricaoObrigatoria
        if (gasto.valor <= BigDecimal.ZERO) return GastoErro.ValorInvalido

        // Valida que a data do gasto pertence ao mês/ano de referência
        if (gasto.dataGasto.monthValue != gasto.mesReferencia ||
            gasto.dataGasto.year != gasto.anoReferencia
        ) return GastoErro.DataForaDeCompetencia

        // Valida que a data de vencimento (quando presente) é >= data do gasto
        if (gasto.dataVencimento != null && gasto.dataVencimento < gasto.dataGasto) {
            return GastoErro.DataVencimentoInvalida
        }

        return null
    }
}
