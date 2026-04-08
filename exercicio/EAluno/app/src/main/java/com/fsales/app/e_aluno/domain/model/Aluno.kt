package com.fsales.app.e_aluno.domain.model

import java.time.LocalDate

data class Aluno(
    val id: Long,
    val nome: String,
    val sobrenome: String,
    val email: String,
    val telefone: String,
    val matricula: String,
    val curso: String,
    val turno: PeriodoTurno,
    val semestre: Semestre,
    val dataNascimento: LocalDate,
    val ativo: Boolean,
    val fotoUrl: String? = null
)
