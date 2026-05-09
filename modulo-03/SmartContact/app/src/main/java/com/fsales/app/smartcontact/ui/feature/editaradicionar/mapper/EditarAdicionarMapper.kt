package com.fsales.app.smartcontact.ui.feature.editaradicionar.mapper

import com.fsales.app.smartcontact.model.Contato
import com.fsales.app.smartcontact.model.Endereco
import com.fsales.app.smartcontact.ui.feature.editaradicionar.state.EditarAdicionarUiState

internal fun Contato.toUiState(): EditarAdicionarUiState = EditarAdicionarUiState(
    id = id,
    nome = nome,
    email = email,
    telefone = telefone,
    dataNascimento = dataNascimento,
    cep = endereco.cep,
    bairro = endereco.bairro,
    logradouro = endereco.logradouro,
    numero = endereco.numero,
    estado = endereco.estado,
    cidade = endereco.cidade,
)

internal fun EditarAdicionarUiState.toDomain(): Contato = Contato(
    id = id,
    nome = nome,
    email = email,
    telefone = telefone,
    dataNascimento = dataNascimento,
    endereco = Endereco(
        cep = cep,
        logradouro = logradouro,
        numero = numero,
        bairro = bairro,
        cidade = cidade,
        estado = estado,
    ),
)

