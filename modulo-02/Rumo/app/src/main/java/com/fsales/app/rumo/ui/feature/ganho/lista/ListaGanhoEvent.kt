package com.fsales.app.rumo.ui.feature.ganho.lista

sealed interface ListaGanhoEvent {
    data object IrParaCadastro : ListaGanhoEvent
    data object MesAnterior : ListaGanhoEvent
    data object ProximoMes : ListaGanhoEvent
    data object TentarNovamente : ListaGanhoEvent
}