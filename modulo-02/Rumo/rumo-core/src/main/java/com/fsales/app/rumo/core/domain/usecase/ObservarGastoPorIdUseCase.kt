package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface ObservarGastoPorIdUseCase {
    operator fun invoke(id: Long): Flow<Gasto?>
}
