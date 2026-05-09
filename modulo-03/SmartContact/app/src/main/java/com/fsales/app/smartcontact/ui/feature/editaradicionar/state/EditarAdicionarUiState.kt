package com.fsales.app.smartcontact.ui.feature.editaradicionar.state

import androidx.annotation.StringRes
import com.fsales.app.smartcontact.R
import java.time.LocalDate

data class EditarAdicionarUiState(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val dataNascimento: LocalDate? = null,
    val cep: String = "",
    val bairro: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val estado: String = "",
    val cidade: String = "",
    val errors: EditarAdicionarErrors = EditarAdicionarErrors()
)

// 1. Sealed class para erros genéricos de campo
sealed class FieldError {
    object Vazio : FieldError()
    object Invalido : FieldError()
    // Adicione outros erros conforme necessário
}

// 2. Data class para armazenar os erros de cada campo
data class EditarAdicionarErrors(
    val nome: FieldError? = null,
    val email: FieldError? = null,
    val telefone: FieldError? = null,
    val dataNascimento: FieldError? = null,
    val cep: FieldError? = null,
    val bairro: FieldError? = null,
    val logradouro: FieldError? = null,
    val numero: FieldError? = null,
    val estado: FieldError? = null,
    val cidade: FieldError? = null
)

@StringRes
fun FieldError.toStringRes(): Int = when (this) {
    FieldError.Vazio   -> R.string.cadastro_edicao_erro_campo_obrigatorio
    FieldError.Invalido -> R.string.cadastro_edicao_erro_campo_invalido
}
