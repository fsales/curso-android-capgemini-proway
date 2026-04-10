package com.fsales.app.rumo.ui.feature.gasto.lista

sealed interface ListaGastoEvent {
    data object Cadastro : ListaGastoEvent
}