package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.ItemExtrato
import kotlinx.coroutines.flow.Flow

interface ListarExtratoPorMesUseCase {
    operator fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<ItemExtrato>>
}
