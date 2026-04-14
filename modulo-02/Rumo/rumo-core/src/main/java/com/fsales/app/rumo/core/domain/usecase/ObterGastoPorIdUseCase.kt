package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Gasto

interface ObterGastoPorIdUseCase {
    suspend operator fun invoke(id: Long): Gasto?
}
