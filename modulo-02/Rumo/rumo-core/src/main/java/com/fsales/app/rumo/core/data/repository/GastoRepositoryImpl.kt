package com.fsales.app.rumo.core.data.repository

import com.fsales.app.rumo.core.data.repository.mapper.toDomain
import com.fsales.app.rumo.core.data.repository.mapper.toEntity
import com.fsales.app.rumo.core.data.room.dao.GastoDao
import com.fsales.app.rumo.core.di.IoDispatcher
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    private val gastoDao: GastoDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GastoRepository {

    override fun listarTodos(): Flow<List<Gasto>> =
        gastoDao.listarTodos().map { gastos -> gastos.map { it.toDomain() } }

    override fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Gasto>> =
        gastoDao.listarPorMes(mesReferencia, anoReferencia)
            .map { gastos -> gastos.map { it.toDomain() } }

    override suspend fun salvar(item: Gasto): Result<Long> = withContext(ioDispatcher) {
        runCatching { gastoDao.salvar(item.toEntity()) }
    }
}
