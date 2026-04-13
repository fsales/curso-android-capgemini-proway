package com.fsales.app.rumo.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(item: GanhoEntity): Long
}
