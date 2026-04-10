package com.fsales.app.rumo.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fsales.app.rumo.core.data.room.entity.GastoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Query("SELECT * FROM gastos ORDER BY dataGasto DESC")
    fun listarTodos(): Flow<List<GastoEntity>>

    @Query(
        "SELECT * FROM gastos WHERE mesReferencia = :mesReferencia AND anoReferencia = :anoReferencia ORDER BY dataGasto DESC"
    )
    fun listarPorMes(mesReferencia: Int, anoReferencia: Int): Flow<List<GastoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(item: GastoEntity): Long
}
