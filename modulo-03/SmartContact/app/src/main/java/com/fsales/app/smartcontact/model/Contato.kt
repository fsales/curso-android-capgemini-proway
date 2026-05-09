package com.fsales.app.smartcontact.model

import java.time.LocalDate

data class Contato(
    val id: Long = 0,
    val nome: String,
    val email: String,
    val telefone: String,
    val dataNascimento: LocalDate? = null,
    val endereco: Endereco = Endereco()
)

data class Endereco(
    val cep: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val estado: String = ""
)

