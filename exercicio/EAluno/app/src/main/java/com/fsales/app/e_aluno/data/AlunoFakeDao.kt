package com.fsales.app.e_aluno.data

import com.fsales.app.e_aluno.data.entity.AlunoEntity
import com.fsales.app.e_aluno.domain.model.PeriodoTurno
import com.fsales.app.e_aluno.domain.model.Semestre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

class AlunoFakeDao @Inject constructor() : AlunoDao {

    override fun getAll(): Flow<List<AlunoEntity>> = flowOf(alunos)

    override suspend fun getBy(id: Long): AlunoEntity? =
        alunos.firstOrNull { it.id == id }

    companion object {
        private val nomes = listOf(
            "João", "Maria", "Pedro", "Ana", "Lucas", "Carla", "Rafael", "Fernanda",
            "Bruno", "Juliana", "Diego", "Patrícia", "Thiago", "Camila", "Gustavo", "Larissa"
        )
        private val sobrenomes = listOf(
            "Silva", "Santos", "Oliveira", "Costa", "Almeida", "Souza", "Pereira", "Lima",
            "Rocha", "Carvalho", "Gomes", "Ribeiro", "Martins", "Araújo", "Barbosa", "Melo"
        )
        private val cursos = listOf("ADS", "SI")
        private val turnos = listOf(PeriodoTurno.MATUTINO, PeriodoTurno.VESPERTINO, PeriodoTurno.NOTURNO)
        private val semestres = listOf(
            Semestre.PRIMEIRO,
            Semestre.SEGUNDO,
            Semestre.TERCEIRO,
            Semestre.QUARTO,
            Semestre.QUINTO,
            Semestre.SEXTO,
            Semestre.SETIMO,
            Semestre.OITAVO
        )

        private val alunos: List<AlunoEntity> = List(80) { index ->
            val id = (index + 1).toLong()
            val nome = nomes[index % nomes.size]
            val sobrenome = sobrenomes[index % sobrenomes.size]

            AlunoEntity(
                id = id,
                nome = nome,
                sobrenome = sobrenome,
                email = "aluno$id@email.com",
                telefone = "(11) 99000-${id.toString().padStart(4, '0')}",
                matricula = "MAT-${id.toString().padStart(4, '0')}",
                curso = cursos[index % cursos.size],
                turno = turnos[index % turnos.size].name,
                semestre = semestres[index % semestres.size].name,
                dataNascimento = LocalDate.of(
                    1998 + (index % 7),
                    (index % 12) + 1,
                    (index % 28) + 1
                ),
                ativo = id % 7L != 0L,
                fotoUrl = if (id % 5L == 0L) {
                    val genero = if (id % 2L == 0L) "women" else "men"
                    "https://randomuser.me/api/portraits/$genero/${(id % 90L) + 1L}.jpg"
                } else {
                    null
                }
            )
        }
    }
}