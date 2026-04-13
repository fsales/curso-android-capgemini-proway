package com.fsales.app.rumo.ui.navigation

import com.fsales.app.rumo.ui.feature.home.HomeEvent
import kotlinx.serialization.Serializable

/**
 * Aba inicial da HomeScreen passada como parâmetro de rota.
 * Usada em [HomeRoute] para evitar strings mágicas na navegação.
 */
@Serializable
enum class AbaInicial {
    GANHOS,
    GASTOS,
    SONHOS;

    fun toHomeEvent(): HomeEvent = when (this) {
        GANHOS -> HomeEvent.IrParaGanhos
        GASTOS -> HomeEvent.IrParaGastos
        SONHOS -> HomeEvent.IrParaSonhos
    }
}


