package com.fsales.app.rumo.ui.feature.gasto.lista

import java.time.YearMonth

sealed interface ListaGastoEvent {
    data object IrParaCadastro    : ListaGastoEvent
    data class  AbrirDetalhe(val id: Long) : ListaGastoEvent
    data object MesAnterior       : ListaGastoEvent
    data object ProximoMes        : ListaGastoEvent
    data class  SelecionarMesAno(val mesAno: YearMonth) : ListaGastoEvent
    data object TentarNovamente   : ListaGastoEvent
}