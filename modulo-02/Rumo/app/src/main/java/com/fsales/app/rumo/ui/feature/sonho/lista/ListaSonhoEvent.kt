package com.fsales.app.rumo.ui.feature.sonho.lista

sealed interface ListaSonhoEvent {
    data class AbrirSonho(val id: Long) : ListaSonhoEvent
}