package com.fsales.app.e_aluno.data.entity

import java.time.LocalDate

data class AlunoEntity(
    val id: Long,
    val nome: String,
    val sobrenome: String,
    val email: String,
    val telefone: String,
    val matricula: String,
    val curso: String,
    val turno: String,
    val semestre: String,
    val dataNascimento: LocalDate,
    val ativo: Boolean,
    val fotoUrl: String? = null
)