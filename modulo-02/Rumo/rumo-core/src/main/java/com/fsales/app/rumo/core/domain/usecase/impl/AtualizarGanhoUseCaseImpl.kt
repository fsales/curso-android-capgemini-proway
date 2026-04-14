package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.GanhoErro
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.AtualizarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.GanhoErroException
import java.math.BigDecimal
import javax.inject.Inject

class AtualizarGanhoUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository,
) : AtualizarGanhoUseCase {

    override suspend operator fun invoke(ganho: Ganho): Result<Unit> {
        validar(ganho)?.let { return Result.failure(GanhoErroException(it)) }
        return ganhoRepository.atualizar(ganho)
    }

    private fun validar(ganho: Ganho): GanhoErro? {
        if (ganho.descricao.isBlank()) return GanhoErro.DescricaoObrigatoria
        if (ganho.valor <= BigDecimal.ZERO) return GanhoErro.ValorInvalido
        return null
    }
}
