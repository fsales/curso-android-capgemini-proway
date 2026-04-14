package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Ganho

interface AtualizarGanhoUseCase {
    suspend operator fun invoke(ganho: Ganho): Result<Unit>
}
