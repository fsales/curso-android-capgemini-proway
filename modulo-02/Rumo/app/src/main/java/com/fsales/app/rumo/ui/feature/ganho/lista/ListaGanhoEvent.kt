package com.fsales.app.rumo.ui.feature.ganho.lista

import java.time.YearMonth

sealed interface ListaGanhoEvent {
    data object IrParaCadastro   : ListaGanhoEvent
    data class  AbrirDetalhe(val id: Long) : ListaGanhoEvent
    data object MesAnterior      : ListaGanhoEvent
    data object ProximoMes       : ListaGanhoEvent
    data class  SelecionarMesAno(val mesAno: YearMonth) : ListaGanhoEvent
    data object TentarNovamente  : ListaGanhoEvent
}