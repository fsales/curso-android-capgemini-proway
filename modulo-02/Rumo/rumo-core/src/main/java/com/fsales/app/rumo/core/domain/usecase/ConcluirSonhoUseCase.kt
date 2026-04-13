package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Sonho

interface ConcluirSonhoUseCase {
    /**
     * Conclui o sonho e registra automaticamente um gasto com o valor da meta,
     * na categoria [com.fsales.app.rumo.core.domain.model.CategoriaGasto.SONHO_REALIZADO],
     * na data e mês de referência atuais.
     */
    suspend operator fun invoke(sonho: Sonho): Result<Unit>
}


