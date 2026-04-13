package com.fsales.app.rumo.ui.feature.home

sealed interface HomeEvent {
    data object IrParaGanhos : HomeEvent
    data object IrParaGastos : HomeEvent
    data object IrParaSonhos : HomeEvent
    data object IrParaSaldo  : HomeEvent
}

