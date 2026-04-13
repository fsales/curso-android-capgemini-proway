package com.fsales.app.rumo.ui.feature.sonho.detalhe

sealed interface DetalheSonhoUiEvent {
    data object NavigateBack : DetalheSonhoUiEvent
}

