package com.fsales.app.rumo.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fsales.app.rumo.core.data.room.entity.GastoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    // Ordenação determinística: primeiro por dataGasto (mais recente primeiro)
    // e em caso de empate por id decrescente.
    @Query("SELECT * FROM gastos ORDER BY dataGasto DESC, id DESC")
    fun listarTodos(): Flow<List<GastoEntity>>

    @Query(
        "SELECT * FROM gastos WHERE mesReferencia = :mesReferencia AND anoReferencia = :anoReferencia ORDER BY dataGasto DESC, id DESC"
    )
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<GastoEntity>>

    @Query("SELECT * FROM gastos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): GastoEntity?

    @Query("SELECT * FROM gastos WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<GastoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(item: GastoEntity): Long

    @Update
    suspend fun atualizar(item: GastoEntity)

    @Query("DELETE FROM gastos WHERE id = :id")
    suspend fun deletar(id: Long)

    @Query("SELECT MAX(dataGasto) FROM gastos WHERE grupoRecorrenciaId = :grupoId")
    suspend fun ultimaDataDoGrupo(grupoId: String): String?

    @Query("SELECT DISTINCT grupoRecorrenciaId FROM gastos WHERE grupoRecorrenciaId IS NOT NULL")
    suspend fun listarGruposAtivos(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(itens: List<GastoEntity>)

    @Query("DELETE FROM gastos WHERE grupoRecorrenciaId = :grupoId")
    suspend fun deletarPorGrupo(grupoId: String)

    @Query("DELETE FROM gastos WHERE grupoRecorrenciaId = :grupoId AND dataGasto >= :corte")
    suspend fun deletarPorGrupoDaquiEmDiante(grupoId: String, corte: String)

    @Query(
        "UPDATE gastos SET grupoRecorrenciaId = :novoGrupoId WHERE grupoRecorrenciaId = :grupoIdAntigo AND dataGasto >= :corte"
    )
    suspend fun atualizarGrupoIdDaquiEmDiante(grupoIdAntigo: String, novoGrupoId: String, corte: String)

    @Query(
        "UPDATE gastos SET descricao = :descricao, valor = :valor, categoria = :categoria, essencial = :essencial, observacao = :observacao " +
        "WHERE grupoRecorrenciaId = :grupoId"
    )
    suspend fun atualizarTodosDoGrupo(
        grupoId: String,
        descricao: String,
        valor: java.math.BigDecimal,
        categoria: com.fsales.app.rumo.core.data.room.entity.enums.CategoriaGasto,
        essencial: Boolean,
        observacao: String?,
    )
}
