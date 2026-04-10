package com.fsales.app.rumo.core.domain.repository

import com.fsales.app.rumo.core.domain.model.Ganho
import kotlinx.coroutines.flow.Flow

interface GanhoRepository {
    fun listarTodos(): Flow<List<Ganho>>
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Ganho>>
    suspend fun salvar(item: Ganho): Result<Long>
}
