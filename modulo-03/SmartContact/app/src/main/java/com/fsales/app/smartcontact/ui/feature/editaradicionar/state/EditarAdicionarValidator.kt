package com.fsales.app.smartcontact.ui.feature.editaradicionar.state

object EditarAdicionarValidator {

    fun validar(state: EditarAdicionarUiState): EditarAdicionarErrors = EditarAdicionarErrors(
        nome       = if (state.nome.isBlank()) FieldError.Vazio else null,
        email      = when {
            state.email.isBlank()          -> FieldError.Vazio
            !state.email.contains("@")     -> FieldError.Invalido
            else                           -> null
        },
        telefone   = if (state.telefone.isBlank()) FieldError.Vazio else null,
        cep        = when {
            state.cep.isBlank()                            -> FieldError.Vazio
            state.cep.filter { it.isDigit() }.length != 8 -> FieldError.Invalido
            else                                           -> null
        },
        bairro     = if (state.bairro.isBlank()) FieldError.Vazio else null,
        logradouro = if (state.logradouro.isBlank()) FieldError.Vazio else null,
        numero     = if (state.numero.isBlank()) FieldError.Vazio else null,
        estado     = if (state.estado.isBlank()) FieldError.Vazio else null,
        cidade     = if (state.cidade.isBlank()) FieldError.Vazio else null,
    )

    fun EditarAdicionarErrors.hasErrors(): Boolean =
        nome != null || email != null || telefone != null ||
        dataNascimento != null || cep != null || bairro != null ||
        logradouro != null || numero != null || estado != null || cidade != null
}

