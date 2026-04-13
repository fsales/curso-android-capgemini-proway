package com.fsales.app.rumo.ui.feature.extrato

sealed interface ExtratoEvent {
    data object MesAnterior : ExtratoEvent
    data object ProximoMes : ExtratoEvent
    data object TentarNovamente : ExtratoEvent
}
