package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.GanhoErroException
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import java.math.BigDecimal
import javax.inject.Inject

class SalvarGanhoUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository
) : SalvarGanhoUseCase {

    override suspend operator fun invoke(ganho: Ganho): Result<Long> {
        validar(ganho)?.let { return Result.failure(GanhoErroException(it)) }
        return ganhoRepository.salvar(ganho)
    }

    private fun validar(ganho: Ganho): GanhoErro? {
        if (ganho.descricao.isBlank()) return GanhoErro.DescricaoObrigatoria
        if (ganho.valor <= BigDecimal.ZERO) return GanhoErro.ValorInvalido

        return runCatching {
            validarCompetencia(ganho.mesReferencia, ganho.anoReferencia)
            require(ganho.dataRecebimento.monthValue == ganho.mesReferencia)
            require(ganho.dataRecebimento.year == ganho.anoReferencia)
        }.exceptionOrNull()?.let {
            if (it is IllegalArgumentException) GanhoErro.DataForaDeCompetencia
            else GanhoErro.CompetenciaInvalida
        }
    }
}
