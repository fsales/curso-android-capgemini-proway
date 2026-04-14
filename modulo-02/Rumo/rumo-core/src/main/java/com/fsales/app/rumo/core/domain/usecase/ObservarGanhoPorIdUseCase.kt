package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Ganho
import kotlinx.coroutines.flow.Flow

interface ObservarGanhoPorIdUseCase {
    operator fun invoke(id: Long): Flow<Ganho?>
}
