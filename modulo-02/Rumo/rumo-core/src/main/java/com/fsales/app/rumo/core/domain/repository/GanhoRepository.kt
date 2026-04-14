package com.fsales.app.rumo.core.domain.repository

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.model.TipoGanho
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

interface GanhoRepository {
    fun listarTodos(): Flow<List<Ganho>>
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Ganho>>
    suspend fun buscarPorId(id: Long): Ganho?
    fun observarPorId(id: Long): Flow<Ganho?>
    suspend fun salvar(item: Ganho): Result<Long>
    suspend fun atualizar(item: Ganho): Result<Unit>
    suspend fun deletar(id: Long): Result<Unit>
    suspend fun ultimaDataDoGrupo(grupoId: String): LocalDate?
    suspend fun listarGruposAtivos(): List<String>
    suspend fun salvarEmLote(itens: List<Ganho>): Result<Unit>
    suspend fun deletarPorGrupo(grupoId: String): Result<Unit>
    suspend fun deletarPorGrupoDaquiEmDiante(grupoId: String, corte: LocalDate): Result<Unit>
    suspend fun atualizarGrupoIdDaquiEmDiante(grupoIdAntigo: String, novoGrupoId: String, corte: LocalDate): Result<Unit>
    suspend fun atualizarTodosDoGrupo(
        grupoId: String,
        descricao: String,
        valor: BigDecimal,
        tipo: TipoGanho,
        observacao: String?,
    ): Result<Unit>
}
