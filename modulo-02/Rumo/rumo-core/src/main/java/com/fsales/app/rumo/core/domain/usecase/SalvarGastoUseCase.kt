package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Gasto

interface SalvarGastoUseCase {
    suspend operator fun invoke(gasto: Gasto): Result<Long>
}