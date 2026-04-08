package com.fsales.app.e_aluno.data

import com.fsales.app.e_aluno.data.entity.AlunoEntity
import com.fsales.app.e_aluno.domain.AlunoRepository
import com.fsales.app.e_aluno.domain.model.Aluno
import com.fsales.app.e_aluno.domain.model.PeriodoTurno
import com.fsales.app.e_aluno.domain.model.Semestre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlunoRepositoryImpl @Inject constructor (
    private val dao: AlunoDao
) : AlunoRepository {

    override fun getAll(): Flow<List<Aluno>> = dao.getAll().map { entities ->
        entities.map { entity ->
            entity.toDomain()
        }.sortedWith(
            compareByDescending<Aluno> { it.ativo }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.nome }
                .thenBy(String.CASE_INSENSITIVE_ORDER) { it.sobrenome }
                .thenBy { it.matricula }
        )

    }

    override suspend fun getBy(id: Long): Result<Aluno?> = runCatching {
        dao.getBy(id)?.toDomain()
    }
}

private fun AlunoEntity.toDomain(): Aluno {
    return Aluno(
        id = id,
        nome = nome,
        sobrenome = sobrenome,
        email = email,
        telefone = telefone,
        matricula = matricula,
        curso = curso,
        turno = PeriodoTurno.fromValue(turno),
        semestre = Semestre.fromValue(semestre),
        dataNascimento = dataNascimento,
        ativo = ativo,
        fotoUrl = fotoUrl
    )
}
