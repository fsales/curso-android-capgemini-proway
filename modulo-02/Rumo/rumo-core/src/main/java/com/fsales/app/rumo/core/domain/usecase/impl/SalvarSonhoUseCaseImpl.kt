package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import com.fsales.app.rumo.core.domain.usecase.SalvarSonhoUseCase
import java.math.BigDecimal
import javax.inject.Inject

class SalvarSonhoUseCaseImpl @Inject constructor(
    private val sonhoRepository: SonhoRepository
) : SalvarSonhoUseCase {

    override suspend fun invoke(sonho: Sonho): Result<Long> {
        val erro = validar(sonho)
        if (erro != null) {
            return Result.failure(IllegalArgumentException(erro))
        }

        return sonhoRepository.salvar(sonho)
    }

    private fun validar(sonho: Sonho): String? {
        if (sonho.titulo.isBlank()) return "O título do sonho é obrigatório."
        if (sonho.valorMeta <= BigDecimal.ZERO) return "O valor meta deve ser maior que zero."
        if (sonho.valorAtual < BigDecimal.ZERO) return "O valor atual não pode ser negativo."

        return null
    }
}
