package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface ListarGastosPorMesUseCase {
    operator fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<Gasto>>
}