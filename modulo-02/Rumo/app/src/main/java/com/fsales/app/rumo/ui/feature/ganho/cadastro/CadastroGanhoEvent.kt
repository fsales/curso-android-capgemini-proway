package com.fsales.app.rumo.ui.feature.ganho.cadastro

sealed interface CadastroGanhoEvent {
    data object Save: CadastroGanhoEvent
}