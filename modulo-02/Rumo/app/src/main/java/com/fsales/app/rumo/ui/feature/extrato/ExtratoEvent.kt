package com.fsales.app.rumo.ui.feature.extrato

import java.time.YearMonth

sealed interface ExtratoEvent {
    data object MesAnterior : ExtratoEvent
    data object ProximoMes : ExtratoEvent
    data class  SelecionarMesAno(val mesAno: YearMonth) : ExtratoEvent
    data object TentarNovamente : ExtratoEvent
}
