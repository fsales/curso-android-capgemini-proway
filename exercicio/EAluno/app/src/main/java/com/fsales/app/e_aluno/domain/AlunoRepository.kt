package com.fsales.app.e_aluno.domain

import com.fsales.app.e_aluno.domain.model.Aluno
import kotlinx.coroutines.flow.Flow

interface AlunoRepository {
    fun getAll(): Flow<List<Aluno>>

    suspend fun getBy(id: Long): Result<Aluno?>
}