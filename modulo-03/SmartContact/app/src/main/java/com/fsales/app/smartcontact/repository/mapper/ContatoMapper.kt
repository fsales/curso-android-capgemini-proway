package com.fsales.app.smartcontact.repository.mapper

import com.fsales.app.smartcontact.data.local.entity.ContatoEntity
import com.fsales.app.smartcontact.model.Contato
import com.fsales.app.smartcontact.model.Endereco

fun ContatoEntity.toDomain(): Contato = Contato(
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

fun Contato.toEntity(): ContatoEntity = ContatoEntity(
    id = id,
    nome = nome,
    email = email,
    telefone = telefone,
    dataNascimento = dataNascimento,
    cep = endereco.cep,
    logradouro = endereco.logradouro,
    numero = endereco.numero,
    bairro = endereco.bairro,
    cidade = endereco.cidade,
    estado = endereco.estado,
)

