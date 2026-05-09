package com.fsales.app.smartcontact.repository

import com.fsales.app.smartcontact.model.Contato
import kotlinx.coroutines.flow.Flow

interface ContatoRepository {
    fun observeContatos(): Flow<List<Contato>>
    suspend fun getContatoById(id: Long): Contato?
    suspend fun saveContato(contato: Contato): Long
    suspend fun deleteContatoById(id: Long)
}

