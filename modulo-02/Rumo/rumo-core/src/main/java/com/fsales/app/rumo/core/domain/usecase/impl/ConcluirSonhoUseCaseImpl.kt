package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import com.fsales.app.rumo.core.domain.usecase.ConcluirSonhoUseCase
import java.time.LocalDate
import javax.inject.Inject

class ConcluirSonhoUseCaseImpl @Inject constructor(
    private val sonhoRepository: SonhoRepository,
    private val gastoRepository: GastoRepository,
) : ConcluirSonhoUseCase {

    override suspend operator fun invoke(sonho: Sonho): Result<Unit> = runCatching {
        val hoje = LocalDate.now()

        // Registra o gasto equivalente ao valor da meta
        val gasto = Gasto(
            descricao     = "Sonho realizado: ${sonho.titulo}",
            valor         = sonho.valorMeta,
            dataGasto     = hoje,
            mesReferencia = hoje.monthValue,
            anoReferencia = hoje.year,
            categoria     = CategoriaGasto.SONHO_REALIZADO,
            essencial     = false,
            recorrente    = false,
            observacao    = sonho.descricao,
        )
        gastoRepository.salvar(gasto).getOrThrow()

        // Marca o sonho como concluído
        sonhoRepository.concluir(sonho.id).getOrThrow()
    }
}


