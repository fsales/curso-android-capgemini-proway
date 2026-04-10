package com.fsales.app.rumo.ui.feature.sonho.lista

sealed interface ListaSonhoEvent {
    data class SonhoClick(val id: Long) : ListaSonhoEvent
}