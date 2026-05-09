package com.fsales.app.smartcontact.repository

import com.fsales.app.smartcontact.data.local.dao.ContatoDao
import com.fsales.app.smartcontact.model.Contato
import com.fsales.app.smartcontact.repository.mapper.toDomain
import com.fsales.app.smartcontact.repository.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContatoRepositoryImpl @Inject constructor(
    private val contatoDao: ContatoDao,
) : ContatoRepository {

    override fun observeContatos(): Flow<List<Contato>> =
        contatoDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getContatoById(id: Long): Contato? =
        contatoDao.findById(id)?.toDomain()

    override suspend fun saveContato(contato: Contato): Long {
        if (contato.id > 0L && contatoDao.findById(contato.id) == null) {
            throw IllegalStateException("contato_para_update_inexistente")
        }

        val upsertedId = contatoDao.upsert(contato.toEntity())
        return if (contato.id > 0L) contato.id else upsertedId
    }

    override suspend fun deleteContatoById(id: Long) {
        contatoDao.deleteById(id)
    }
}
