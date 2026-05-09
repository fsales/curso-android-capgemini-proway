package com.fsales.app.smartcontact.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fsales.app.smartcontact.data.local.entity.ContatoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContatoDao {

    @Query("SELECT * FROM contatos ORDER BY nome ASC")
    fun observeAll(): Flow<List<ContatoEntity>>

    @Query("SELECT * FROM contatos WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): ContatoEntity?

    @Upsert
    suspend fun upsert(contato: ContatoEntity): Long

    @Query("DELETE FROM contatos WHERE id = :id")
    suspend fun deleteById(id: Long)
}
