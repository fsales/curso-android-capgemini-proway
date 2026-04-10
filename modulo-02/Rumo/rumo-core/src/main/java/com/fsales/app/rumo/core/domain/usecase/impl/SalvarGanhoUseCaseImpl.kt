package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import java.math.BigDecimal
import javax.inject.Inject

class SalvarGanhoUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository
) : SalvarGanhoUseCase {

    override suspend fun invoke(ganho: Ganho): Result<Long> {
        validar(ganho)?.let { return Result.failure(IllegalArgumentException(it)) }
        return ganhoRepository.salvar(ganho)
    }

    private fun validar(ganho: Ganho): String? {
        if (ganho.descricao.isBlank()) return "A descrição do ganho é obrigatória."
        if (ganho.valor <= BigDecimal.ZERO) return "O valor do ganho deve ser maior que zero."

        return runCatching {
            validarCompetencia(ganho.mesReferencia, ganho.anoReferencia)
            require(ganho.dataRecebimento.monthValue == ganho.mesReferencia) {
                "O mês de referência deve corresponder à data de recebimento."
            }
            require(ganho.dataRecebimento.year == ganho.anoReferencia) {
                "O ano de referência deve corresponder à data de recebimento."
            }
        }.exceptionOrNull()?.message
    }
}
