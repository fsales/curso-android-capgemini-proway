package com.fsales.app.e_aluno.domain.model

enum class Semestre {
    PRIMEIRO,
    SEGUNDO,
    TERCEIRO,
    QUARTO,
    QUINTO,
    SEXTO,
    SETIMO,
    OITAVO;

    companion object {
        fun fromValue(value: String): Semestre {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PRIMEIRO
        }
    }
}