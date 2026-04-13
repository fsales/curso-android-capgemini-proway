package com.fsales.app.rumo.ui.feature.gasto.lista

sealed interface ListaGastoEvent {
    data object IrParaCadastro   : ListaGastoEvent
    data object MesAnterior      : ListaGastoEvent
    data object ProximoMes       : ListaGastoEvent
    data object TentarNovamente  : ListaGastoEvent
}