package com.fsales.app.rumo.ui.feature.gasto.detalhe

sealed interface DetalheGastoEvent {
    data object Editar                 : DetalheGastoEvent
    data object ExcluirEste            : DetalheGastoEvent
    data object ExcluirTodos           : DetalheGastoEvent
    data object ExcluirDaquiEmDiante   : DetalheGastoEvent
    data object ConfirmarExclusao      : DetalheGastoEvent
    data object CancelarDialog         : DetalheGastoEvent
    data object Voltar                 : DetalheGastoEvent
}
