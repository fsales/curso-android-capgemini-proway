package com.fsales.app.rumo.core.domain.repository

import com.fsales.app.rumo.core.domain.model.Sonho
import kotlinx.coroutines.flow.Flow

interface SonhoRepository {
    fun listarTodos(): Flow<List<Sonho>>
    suspend fun salvar(item: Sonho): Result<Long>
}
