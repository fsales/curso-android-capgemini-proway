package com.fsales.app.rumo.core.domain.repository

import com.fsales.app.rumo.core.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface GastoRepository {
    fun listarTodos(): Flow<List<Gasto>>
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Gasto>>
    suspend fun salvar(item: Gasto): Result<Long>
}
