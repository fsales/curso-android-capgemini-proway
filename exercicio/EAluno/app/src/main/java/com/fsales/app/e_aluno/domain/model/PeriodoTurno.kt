package com.fsales.app.e_aluno.domain.model

enum class PeriodoTurno {
    MATUTINO,
    VESPERTINO,
    NOTURNO;

    companion object {
        fun fromValue(value: String): PeriodoTurno {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MATUTINO
        }
    }
}
