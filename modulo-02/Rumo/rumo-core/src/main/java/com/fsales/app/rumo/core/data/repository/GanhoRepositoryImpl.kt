package com.fsales.app.rumo.core.data.repository

import com.fsales.app.rumo.core.data.repository.mapper.toDomain
import com.fsales.app.rumo.core.data.repository.mapper.toEntity
import com.fsales.app.rumo.core.data.room.dao.GanhoDao
import com.fsales.app.rumo.core.di.IoDispatcher
import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class GanhoRepositoryImpl @Inject constructor(
    private val ganhoDao: GanhoDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : GanhoRepository {

    override fun listarTodos(): Flow<List<Ganho>> =
        ganhoDao.listarTodos().map { it.map { e -> e.toDomain() } }

    override fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Ganho>> =
        ganhoDao.listarPorMes(mesReferencia, anoReferencia).map { it.map { e -> e.toDomain() } }

    override suspend fun buscarPorId(id: Long): Ganho? = withContext(ioDispatcher) {
        ganhoDao.buscarPorId(id)?.toDomain()
    }

    override fun observarPorId(id: Long): Flow<Ganho?> =
        ganhoDao.observarPorId(id).map { it?.toDomain() }

    override suspend fun salvar(item: Ganho): Result<Long> = withContext(ioDispatcher) {
        runCatching { ganhoDao.salvar(item.toEntity()) }
    }

    override suspend fun atualizar(item: Ganho): Result<Unit> = withContext(ioDispatcher) {
        runCatching { ganhoDao.atualizar(item.toEntity()) }
    }

    override suspend fun deletar(id: Long): Result<Unit> = withContext(ioDispatcher) {
        runCatching { ganhoDao.deletar(id) }
    }

    override suspend fun ultimaDataDoGrupo(grupoId: String): LocalDate? = withContext(ioDispatcher) {
        ganhoDao.ultimaDataDoGrupo(grupoId)?.let { LocalDate.parse(it) }
    }

    override suspend fun listarGruposAtivos(): List<String> = withContext(ioDispatcher) {
        ganhoDao.listarGruposAtivos()
    }

    override suspend fun salvarEmLote(itens: List<Ganho>): Result<Unit> = withContext(ioDispatcher) {
        runCatching { ganhoDao.insertAll(itens.map { it.toEntity() }) }
    }

    override suspend fun deletarPorGrupo(grupoId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching { ganhoDao.deletarPorGrupo(grupoId) }
    }

    override suspend fun deletarPorGrupoDaquiEmDiante(grupoId: String, corte: LocalDate): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching { ganhoDao.deletarPorGrupoDaquiEmDiante(grupoId, corte.toString()) }
        }

    override suspend fun atualizarGrupoIdDaquiEmDiante(
        grupoIdAntigo: String,
        novoGrupoId: String,
        corte: LocalDate,
    ): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            ganhoDao.atualizarGrupoIdDaquiEmDiante(grupoIdAntigo, novoGrupoId, corte.toString())
        }
    }

    override suspend fun atualizarTodosDoGrupo(
        grupoId: String,
        descricao: String,
        valor: BigDecimal,
        tipo: TipoGanho,
        observacao: String?,
    ): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            ganhoDao.atualizarTodosDoGrupo(
                grupoId = grupoId,
                descricao = descricao,
                valor = valor,
                tipo = com.fsales.app.rumo.core.data.room.entity.enums.TipoGanho.valueOf(tipo.name),
                observacao = observacao,
            )
        }
    }
}

