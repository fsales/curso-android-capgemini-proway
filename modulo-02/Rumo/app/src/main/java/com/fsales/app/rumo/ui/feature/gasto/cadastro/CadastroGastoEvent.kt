package com.fsales.app.rumo.ui.feature.gasto.cadastro

import com.fsales.app.rumo.core.domain.model.CategoriaGasto
import java.time.LocalDate

sealed interface CadastroGastoEvent {
    data class AlterarDescricao(val valor: String)       : CadastroGastoEvent
    data class AlterarValor(val valor: String)           : CadastroGastoEvent
    data class AlterarData(val data: LocalDate)          : CadastroGastoEvent
    data class AlterarCategoria(val categoria: CategoriaGasto) : CadastroGastoEvent
    data class AlterarEssencial(val essencial: Boolean)  : CadastroGastoEvent
    data class AlterarRecorrente(val recorrente: Boolean): CadastroGastoEvent
    data class AlterarObservacao(val observacao: String) : CadastroGastoEvent
    data object Salvar : CadastroGastoEvent
    data object Voltar : CadastroGastoEvent
}