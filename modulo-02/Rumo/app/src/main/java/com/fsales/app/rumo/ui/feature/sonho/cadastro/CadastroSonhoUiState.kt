package com.fsales.app.rumo.ui.feature.sonho.cadastro

import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import java.time.LocalDate

data class CadastroSonhoUiState(
    val titulo: String = "",
    val descricao: String = "",
    val valorMetaTexto: String = "",
    val prioridade: PrioridadeSonho = PrioridadeSonho.MEDIA,
    val prazoAlvo: LocalDate? = null,
    val erros: Map<String, String> = emptyMap(),
    val salvando: Boolean = false,
)

