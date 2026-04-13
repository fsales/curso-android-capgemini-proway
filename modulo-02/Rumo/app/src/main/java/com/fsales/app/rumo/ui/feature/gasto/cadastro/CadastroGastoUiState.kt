package com.fsales.app.rumo.ui.feature.gasto.cadastro

import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import java.time.LocalDate

data class CadastroGastoUiState(
    val descricao: String = "",
    val valorTexto: String = "",
    val dataGasto: LocalDate = LocalDate.now(),
    val categoria: CategoriaGasto = CategoriaGasto.OUTROS,
    val essencial: Boolean = false,
    val recorrente: Boolean = false,
    val observacao: String = "",
    val erros: Map<String, String> = emptyMap(),
    val salvando: Boolean = false,
)

