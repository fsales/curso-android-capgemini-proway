package com.fsales.app.rumo.ui.feature.home

sealed interface HomeEvent {
    data object IrParaSaldo    : HomeEvent
    data object IrParaExtrato  : HomeEvent
    data object IrParaGanhos   : HomeEvent
    data object IrParaGastos   : HomeEvent
    data object IrParaSonhos   : HomeEvent
}

