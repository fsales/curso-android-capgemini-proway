package com.fsales.app.rumo.ui.feature.sonho.cadastro

import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import com.fsales.app.rumo.core.domain.model.SonhoErro
import java.time.LocalDate

const val ERRO_TITULO     = "erro_titulo"
const val ERRO_VALOR_META = "erro_valor_meta"

data class CadastroSonhoUiState(
    val titulo: String = "",
    val descricao: String = "",
    val valorMetaTexto: String = "",
    val prioridade: PrioridadeSonho = PrioridadeSonho.MEDIA,
    val prazoAlvo: LocalDate? = null,
    val erros: Map<String, SonhoErro> = emptyMap(),
    val salvando: Boolean = false,
)

