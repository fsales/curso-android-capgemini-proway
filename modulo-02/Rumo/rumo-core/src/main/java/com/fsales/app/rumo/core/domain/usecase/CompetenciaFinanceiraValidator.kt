package com.fsales.app.rumo.core.domain.usecase

internal fun validarCompetencia(mesReferencia: Int, anoReferencia: Int) {
    require(mesReferencia in 1..12) { "Mês de referência inválido." }
    require(anoReferencia > 0) { "Ano de referência inválido." }
}