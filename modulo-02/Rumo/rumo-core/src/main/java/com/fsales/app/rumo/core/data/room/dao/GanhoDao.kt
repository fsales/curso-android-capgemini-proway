package com.fsales.app.rumo.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fsales.app.rumo.core.data.room.entity.GanhoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GanhoDao {

    // Ordenação determinística: primeiro por dataRecebimento (mais recente primeiro)
    // e em caso de empate por id decrescente.
    @Query("SELECT * FROM ganhos ORDER BY dataRecebimento DESC, id DESC")
    fun listarTodos(): Flow<List<GanhoEntity>>

    @Query(
        "SELECT * FROM ganhos WHERE mesReferencia = :mesReferencia AND anoReferencia = :anoReferencia ORDER BY dataRecebimento DESC, id DESC"
    )
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<GanhoEntity>>

    @Query("SELECT * FROM ganhos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Long): GanhoEntity?

    @Query("SELECT * FROM ganhos WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<GanhoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(item: GanhoEntity): Long

    @Update
    suspend fun atualizar(item: GanhoEntity)

    @Query("DELETE FROM ganhos WHERE id = :id")
    suspend fun deletar(id: Long)

    @Query("SELECT MAX(dataRecebimento) FROM ganhos WHERE grupoRecorrenciaId = :grupoId")
    suspend fun ultimaDataDoGrupo(grupoId: String): String?

    @Query("SELECT DISTINCT grupoRecorrenciaId FROM ganhos WHERE grupoRecorrenciaId IS NOT NULL")
    suspend fun listarGruposAtivos(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(itens: List<GanhoEntity>)

    @Query("DELETE FROM ganhos WHERE grupoRecorrenciaId = :grupoId")
    suspend fun deletarPorGrupo(grupoId: String)

    @Query("DELETE FROM ganhos WHERE grupoRecorrenciaId = :grupoId AND dataRecebimento >= :corte")
    suspend fun deletarPorGrupoDaquiEmDiante(grupoId: String, corte: String)

    @Query(
        "UPDATE ganhos SET grupoRecorrenciaId = :novoGrupoId WHERE grupoRecorrenciaId = :grupoIdAntigo AND dataRecebimento >= :corte"
    )
    suspend fun atualizarGrupoIdDaquiEmDiante(grupoIdAntigo: String, novoGrupoId: String, corte: String)

    @Query(
        "UPDATE ganhos SET descricao = :descricao, valor = :valor, tipo = :tipo, observacao = :observacao " +
        "WHERE grupoRecorrenciaId = :grupoId"
    )
    suspend fun atualizarTodosDoGrupo(
        grupoId: String,
        descricao: String,
        valor: java.math.BigDecimal,
        tipo: com.fsales.app.rumo.core.data.room.entity.enums.TipoGanho,
        observacao: String?,
    )
}
