package com.fsales.app.rumo.core.data.repository

import com.fsales.app.rumo.core.data.repository.mapper.toDomain
import com.fsales.app.rumo.core.data.repository.mapper.toEntity
import com.fsales.app.rumo.core.data.room.dao.GastoDao
import com.fsales.app.rumo.core.di.IoDispatcher
import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    private val gastoDao: GastoDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : GastoRepository {

    override fun listarTodos(): Flow<List<Gasto>> =
        gastoDao.listarTodos().map { it.map { e -> e.toDomain() } }

    override fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Gasto>> =
        gastoDao.listarPorMes(mesReferencia, anoReferencia).map { it.map { e -> e.toDomain() } }

    override suspend fun buscarPorId(id: Long): Gasto? = withContext(ioDispatcher) {
        gastoDao.buscarPorId(id)?.toDomain()
    }

    override fun observarPorId(id: Long): Flow<Gasto?> =
        gastoDao.observarPorId(id).map { it?.toDomain() }

    override suspend fun salvar(item: Gasto): Result<Long> = withContext(ioDispatcher) {
        runCatching { gastoDao.salvar(item.toEntity()) }
    }

    override suspend fun atualizar(item: Gasto): Result<Unit> = withContext(ioDispatcher) {
        runCatching { gastoDao.atualizar(item.toEntity()) }
    }

    override suspend fun deletar(id: Long): Result<Unit> = withContext(ioDispatcher) {
        runCatching { gastoDao.deletar(id) }
    }

    override suspend fun ultimaDataDoGrupo(grupoId: String): LocalDate? = withContext(ioDispatcher) {
        gastoDao.ultimaDataDoGrupo(grupoId)?.let { LocalDate.parse(it) }
    }

    override suspend fun listarGruposAtivos(): List<String> = withContext(ioDispatcher) {
        gastoDao.listarGruposAtivos()
    }

    override suspend fun salvarEmLote(itens: List<Gasto>): Result<Unit> = withContext(ioDispatcher) {
        runCatching { gastoDao.insertAll(itens.map { it.toEntity() }) }
    }

    override suspend fun deletarPorGrupo(grupoId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching { gastoDao.deletarPorGrupo(grupoId) }
    }

    override suspend fun deletarPorGrupoDaquiEmDiante(grupoId: String, corte: LocalDate): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching { gastoDao.deletarPorGrupoDaquiEmDiante(grupoId, corte.toString()) }
        }

    override suspend fun atualizarGrupoIdDaquiEmDiante(
        grupoIdAntigo: String,
        novoGrupoId: String,
        corte: LocalDate,
    ): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            gastoDao.atualizarGrupoIdDaquiEmDiante(grupoIdAntigo, novoGrupoId, corte.toString())
        }
    }

    override suspend fun atualizarTodosDoGrupo(
        grupoId: String,
        descricao: String,
        valor: BigDecimal,
        categoria: CategoriaGasto,
        essencial: Boolean,
        observacao: String?,
    ): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            gastoDao.atualizarTodosDoGrupo(
                grupoId = grupoId,
                descricao = descricao,
                valor = valor,
                categoria = com.fsales.app.rumo.core.data.room.entity.enums.CategoriaGasto.valueOf(categoria.name),
                essencial = essencial,
                observacao = observacao,
            )
        }
    }
}

