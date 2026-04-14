package com.fsales.app.rumo.core.domain.repository

import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import com.fsales.app.rumo.core.domain.model.Gasto
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

interface GastoRepository {
    fun listarTodos(): Flow<List<Gasto>>
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<Gasto>>
    suspend fun buscarPorId(id: Long): Gasto?
    fun observarPorId(id: Long): Flow<Gasto?>
    suspend fun salvar(item: Gasto): Result<Long>
    suspend fun atualizar(item: Gasto): Result<Unit>
    suspend fun deletar(id: Long): Result<Unit>
    suspend fun ultimaDataDoGrupo(grupoId: String): LocalDate?
    suspend fun listarGruposAtivos(): List<String>
    suspend fun salvarEmLote(itens: List<Gasto>): Result<Unit>
    suspend fun deletarPorGrupo(grupoId: String): Result<Unit>
    suspend fun deletarPorGrupoDaquiEmDiante(grupoId: String, corte: LocalDate): Result<Unit>
    suspend fun atualizarGrupoIdDaquiEmDiante(grupoIdAntigo: String, novoGrupoId: String, corte: LocalDate): Result<Unit>
    suspend fun atualizarTodosDoGrupo(
        grupoId: String,
        descricao: String,
        valor: BigDecimal,
        categoria: CategoriaGasto,
        essencial: Boolean,
        observacao: String?,
    ): Result<Unit>
}
