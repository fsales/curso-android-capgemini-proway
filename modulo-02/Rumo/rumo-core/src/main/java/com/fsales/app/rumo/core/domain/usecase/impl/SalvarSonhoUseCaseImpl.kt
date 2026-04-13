package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.model.SonhoErro
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import com.fsales.app.rumo.core.domain.usecase.SalvarSonhoUseCase
import com.fsales.app.rumo.core.domain.usecase.SonhoErroException
import java.math.BigDecimal
import javax.inject.Inject

class SalvarSonhoUseCaseImpl @Inject constructor(
    private val sonhoRepository: SonhoRepository
) : SalvarSonhoUseCase {

    override suspend fun invoke(sonho: Sonho): Result<Long> {
        validar(sonho)?.let { return Result.failure(SonhoErroException(it)) }
        return sonhoRepository.salvar(sonho)
    }

    private fun validar(sonho: Sonho): SonhoErro? {
        if (sonho.titulo.isBlank()) return SonhoErro.TituloObrigatorio
        if (sonho.valorMeta <= BigDecimal.ZERO) return SonhoErro.ValorMetaInvalido
        return null
    }
}
