package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Sonho
import kotlinx.coroutines.flow.Flow

interface ObterSonhoUseCase {
    operator fun invoke(id: Long): Flow<Sonho?>
}

