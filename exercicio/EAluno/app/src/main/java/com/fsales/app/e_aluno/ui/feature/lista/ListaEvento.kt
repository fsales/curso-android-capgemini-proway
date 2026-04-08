package com.fsales.app.e_aluno.ui.feature.lista

sealed interface ListaEvento {
    data class Detalhes(val id: Long) : ListaEvento
}