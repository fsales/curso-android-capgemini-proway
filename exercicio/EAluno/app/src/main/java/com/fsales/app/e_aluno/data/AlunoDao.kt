package com.fsales.app.e_aluno.data

import com.fsales.app.e_aluno.data.entity.AlunoEntity
import kotlinx.coroutines.flow.Flow

interface AlunoDao {
    fun getAll(): Flow<List<AlunoEntity>>
    suspend fun getBy(id: Long): AlunoEntity?
}