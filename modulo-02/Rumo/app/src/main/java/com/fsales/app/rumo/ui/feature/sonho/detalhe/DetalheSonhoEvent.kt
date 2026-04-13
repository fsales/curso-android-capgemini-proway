package com.fsales.app.rumo.ui.feature.sonho.detalhe

sealed interface DetalheSonhoEvent {
    data object Concluir           : DetalheSonhoEvent
    data object ConfirmarConclusao : DetalheSonhoEvent
    data object CancelarConclusao  : DetalheSonhoEvent
    data object Voltar             : DetalheSonhoEvent
}

