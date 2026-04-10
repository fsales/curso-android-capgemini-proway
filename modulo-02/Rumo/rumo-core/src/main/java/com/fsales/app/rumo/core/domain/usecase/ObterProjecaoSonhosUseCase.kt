package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.ProjecaoSonho
import kotlinx.coroutines.flow.Flow

interface ObterProjecaoSonhosUseCase {
    operator fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<ProjecaoSonho>>
}
