package com.fsales.app.rumo.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fsales.app.rumo.core.data.room.entity.SonhoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SonhoDao {

    @Query("SELECT * FROM sonhos ORDER BY dataCriacao DESC")
    fun listarTodos(): Flow<List<SonhoEntity>>

    @Query("SELECT * FROM sonhos WHERE id = :id")
    fun obterPorId(id: Long): Flow<SonhoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(item: SonhoEntity): Long

    @Query("UPDATE sonhos SET concluido = 1 WHERE id = :id")
    suspend fun concluir(id: Long)
}
