package com.fsales.app.rumo.ui.feature.ganho.lista

sealed interface  ListaGanhoEvent {

    data object Cadastro : ListaGanhoEvent
}