package com.fsales.app.rumo.core.domain.repository

import com.fsales.app.rumo.core.domain.model.Sonho
import kotlinx.coroutines.flow.Flow

interface SonhoRepository {
    fun listarTodos(): Flow<List<Sonho>>
    fun obterPorId(id: Long): Flow<Sonho?>
    suspend fun salvar(item: Sonho): Result<Long>
    suspend fun concluir(id: Long): Result<Unit>
}
