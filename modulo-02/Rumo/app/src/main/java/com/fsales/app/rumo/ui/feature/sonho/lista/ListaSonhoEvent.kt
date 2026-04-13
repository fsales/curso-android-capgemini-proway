package com.fsales.app.rumo.ui.feature.sonho.lista

sealed interface ListaSonhoEvent {
    data object IrParaCadastro          : ListaSonhoEvent
    data object TentarNovamente         : ListaSonhoEvent
    data class AbrirSonho(val id: Long) : ListaSonhoEvent
}