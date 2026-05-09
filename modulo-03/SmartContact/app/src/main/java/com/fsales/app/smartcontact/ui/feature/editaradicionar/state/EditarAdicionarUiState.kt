package com.fsales.app.smartcontact.ui.feature.editaradicionar.state

import androidx.annotation.StringRes
import com.fsales.app.smartcontact.R
import java.time.LocalDate

data class EditarAdicionarUiState(
    val id: Long = 0L,
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
    /** Indica que uma consulta ao ViaCEP está em andamento */
    val carregandoCep: Boolean = false,
    val errors: EditarAdicionarErrors = EditarAdicionarErrors()
)

// 1. Sealed class para erros genéricos de campo
sealed class FieldError {
    object Vazio : FieldError()
    object Invalido : FieldError()
    object CepNaoEncontrado : FieldError()
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
    FieldError.Vazio            -> R.string.cadastro_edicao_erro_campo_obrigatorio
    FieldError.Invalido         -> R.string.cadastro_edicao_erro_campo_invalido
    FieldError.CepNaoEncontrado -> R.string.cadastro_edicao_erro_cep_nao_encontrado
}
