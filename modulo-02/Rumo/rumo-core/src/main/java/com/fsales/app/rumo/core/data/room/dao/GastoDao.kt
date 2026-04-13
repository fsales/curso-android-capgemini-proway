package com.fsales.app.rumo.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(item: GastoEntity): Long
}
