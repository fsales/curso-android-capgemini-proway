package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Ganho
import kotlinx.coroutines.flow.Flow

interface ListarGanhosPorMesUseCase {
    operator fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<Ganho>>
}