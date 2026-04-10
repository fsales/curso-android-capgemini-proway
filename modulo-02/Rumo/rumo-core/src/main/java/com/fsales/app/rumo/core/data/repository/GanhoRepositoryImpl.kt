package com.fsales.app.rumo.core.data.repository

import com.fsales.app.rumo.core.data.repository.mapper.toDomain
import com.fsales.app.rumo.core.data.repository.mapper.toEntity
import com.fsales.app.rumo.core.data.room.dao.GanhoDao
import com.fsales.app.rumo.core.di.IoDispatcher
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GanhoRepositoryImpl @Inject constructor(
    private val ganhoDao: GanhoDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GanhoRepository {

    override fun listarTodos(): Flow<List<Ganho>> =
        ganhoDao.listarTodos().map { ganhos -> ganhos.map { it.toDomain() } }

    override fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Ganho>> =
        ganhoDao.listarPorMes(mesReferencia, anoReferencia)
            .map { ganhos -> ganhos.map { it.toDomain() } }

    override suspend fun salvar(item: Ganho): Result<Long> = withContext(ioDispatcher) {
        runCatching { ganhoDao.salvar(item.toEntity()) }
    }
}
