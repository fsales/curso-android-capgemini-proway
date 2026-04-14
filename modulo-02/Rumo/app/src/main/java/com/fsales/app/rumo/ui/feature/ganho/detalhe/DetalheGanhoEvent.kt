package com.fsales.app.rumo.ui.feature.ganho.detalhe

sealed interface DetalheGanhoEvent {
    data object Editar                 : DetalheGanhoEvent
    data object ExcluirEste            : DetalheGanhoEvent
    data object ExcluirTodos           : DetalheGanhoEvent
    data object ExcluirDaquiEmDiante   : DetalheGanhoEvent
    data object ConfirmarExclusao      : DetalheGanhoEvent
    data object CancelarDialog         : DetalheGanhoEvent
    data object Voltar                 : DetalheGanhoEvent
}
