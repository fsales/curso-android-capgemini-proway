package com.fsales.app.rumo.ui.feature.sonho.cadastro

import com.fsales.app.rumo.core.domain.model.PrioridadeSonho
import java.time.LocalDate

sealed interface CadastroSonhoEvent {
    data class AlterarTitulo(val valor: String)             : CadastroSonhoEvent
    data class AlterarDescricao(val valor: String)          : CadastroSonhoEvent
    data class AlterarValorMeta(val valor: String)          : CadastroSonhoEvent
    data class AlterarValorAtual(val valor: String)         : CadastroSonhoEvent
    data class AlterarPrioridade(val prioridade: PrioridadeSonho) : CadastroSonhoEvent
    data class AlterarPrazo(val prazo: LocalDate?)          : CadastroSonhoEvent
    data object Salvar : CadastroSonhoEvent
    data object Voltar : CadastroSonhoEvent
}