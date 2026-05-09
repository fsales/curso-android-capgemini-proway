package com.fsales.app.smartcontact.ui.feature.lista.state

import com.fsales.app.smartcontact.model.Contato

data class ListUiState(
    val contatos: List<Contato> = emptyList(),
    val carregando: Boolean = false,
    val erro: String? = null,
)