package com.fsales.app.smartcontact.ui.feature.editaradicionar

import java.time.LocalDate

sealed interface EditarAdicionarEvent {
    data class AlterarNome(val valor: String)          : EditarAdicionarEvent
    data class AlterarEmail(val valor: String)         : EditarAdicionarEvent
    data class AlterarTelefone(val valor: String)      : EditarAdicionarEvent
    data class AlterarDataNascimento(val data: LocalDate) : EditarAdicionarEvent
    data class AlterarCep(val valor: String)           : EditarAdicionarEvent
    data class AlterarBairro(val valor: String)        : EditarAdicionarEvent
    data class AlterarLogradouro(val valor: String)    : EditarAdicionarEvent
    data class AlterarNumero(val valor: String)        : EditarAdicionarEvent
    data class AlterarEstado(val valor: String)        : EditarAdicionarEvent
    data class AlterarCidade(val valor: String)        : EditarAdicionarEvent
    data object Salvar                                 : EditarAdicionarEvent
    data object Voltar                                 : EditarAdicionarEvent
}

