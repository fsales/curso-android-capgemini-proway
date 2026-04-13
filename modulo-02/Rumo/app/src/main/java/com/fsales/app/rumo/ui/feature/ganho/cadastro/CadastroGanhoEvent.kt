package com.fsales.app.rumo.ui.feature.ganho.cadastro

import com.fsales.app.rumo.core.domain.model.TipoGanho
import java.time.LocalDate

sealed interface CadastroGanhoEvent {
    data class AlterarDescricao(val valor: String) : CadastroGanhoEvent
    data class AlterarValor(val valor: String) : CadastroGanhoEvent
    data class AlterarData(val data: LocalDate) : CadastroGanhoEvent
    data class AlterarTipo(val tipo: TipoGanho) : CadastroGanhoEvent
    data class AlterarRecorrente(val recorrente: Boolean) : CadastroGanhoEvent
    data class AlterarObservacao(val observacao: String) : CadastroGanhoEvent
    data object Salvar : CadastroGanhoEvent
    data object Voltar : CadastroGanhoEvent
}