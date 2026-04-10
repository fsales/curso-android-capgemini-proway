package com.fsales.app.rumo.core.data.repository

import com.fsales.app.rumo.core.data.repository.mapper.toDomain
import com.fsales.app.rumo.core.data.repository.mapper.toEntity
import com.fsales.app.rumo.core.data.room.dao.SonhoDao
import com.fsales.app.rumo.core.di.IoDispatcher
import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SonhoRepositoryImpl @Inject constructor(
    private val sonhoDao: SonhoDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SonhoRepository {

    override fun listarTodos(): Flow<List<Sonho>> =
        sonhoDao.listarTodos().map { sonhos -> sonhos.map { it.toDomain() } }

    override suspend fun salvar(item: Sonho): Result<Long> = withContext(ioDispatcher) {
        runCatching { sonhoDao.salvar(item.toEntity()) }
    }
}
