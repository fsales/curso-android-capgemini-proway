package com.fsales.app.e_aluno.ui.preview

import com.fsales.app.e_aluno.domain.model.Aluno
import com.fsales.app.e_aluno.domain.model.PeriodoTurno
import com.fsales.app.e_aluno.domain.model.Semestre
import java.time.LocalDate

object PreviewData {
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

    val alunos: List<Aluno> = List(20) { index ->
        val id = (index + 1).toLong()
        Aluno(
            id = id,
            nome = nomes[index % nomes.size],
            sobrenome = sobrenomes[index % sobrenomes.size],
            email = "aluno$id@email.com",
            telefone = "(11) 99000-${id.toString().padStart(4, '0')}",
            matricula = "MAT-${id.toString().padStart(4, '0')}",
            curso = cursos[index % cursos.size],
            turno = turnos[index % turnos.size],
            semestre = semestres[index % semestres.size],
            dataNascimento = LocalDate.of(1998 + (index % 7), (index % 12) + 1, (index % 28) + 1),
            ativo = id % 7L != 0L,
            fotoUrl = if (id % 4L == 0L) {
                val genero = if (index % 2 == 0) "women" else "men"
                "https://randomuser.me/api/portraits/$genero/${(id % 90L) + 1L}.jpg"
            } else null
        )
    }
}
