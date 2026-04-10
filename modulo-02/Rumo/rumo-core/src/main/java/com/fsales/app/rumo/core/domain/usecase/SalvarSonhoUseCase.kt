package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Sonho

interface SalvarSonhoUseCase {
    suspend operator fun invoke(sonho: Sonho): Result<Long>
}