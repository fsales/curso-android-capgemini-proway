package com.fsales.app.rumo.core.domain.usecase

import com.fsales.app.rumo.core.domain.model.SaldoMensal
import kotlinx.coroutines.flow.Flow

interface ObterSaldoMensalUseCase {
    operator fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<SaldoMensal>
}