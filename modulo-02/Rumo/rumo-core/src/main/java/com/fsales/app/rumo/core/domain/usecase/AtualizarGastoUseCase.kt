package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Gasto

interface AtualizarGastoUseCase {
    suspend operator fun invoke(gasto: Gasto): Result<Unit>
}
