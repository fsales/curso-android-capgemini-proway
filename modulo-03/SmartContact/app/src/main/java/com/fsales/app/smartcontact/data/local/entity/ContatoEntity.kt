package com.fsales.app.smartcontact.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "contatos")
data class ContatoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String,
    val email: String,
    val telefone: String,
    val dataNascimento: LocalDate? = null,
    val cep: String,
    val logradouro: String,
    val numero: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
)

