package com.fsales.app.rumo.ui.navigation

import com.fsales.app.rumo.ui.feature.home.HomeEvent
import kotlinx.serialization.Serializable

/**
 * Aba inicial da HomeScreen passada como parâmetro de rota.
 * Usada em [HomeRoute] para evitar strings mágicas na navegação.
 */
@Serializable
enum class AbaInicial {
    HOME,
    EXTRATO,
    GANHOS,
    GASTOS,
    SONHOS;

    fun toHomeEvent(): HomeEvent = when (this) {
        HOME    -> HomeEvent.IrParaHome
        EXTRATO -> HomeEvent.IrParaExtrato
        GANHOS  -> HomeEvent.IrParaGanhos
        GASTOS  -> HomeEvent.IrParaGastos
        SONHOS  -> HomeEvent.IrParaSonhos
    }
}


