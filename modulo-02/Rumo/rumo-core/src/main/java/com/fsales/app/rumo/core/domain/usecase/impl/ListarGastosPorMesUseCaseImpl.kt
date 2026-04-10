package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.ListarGastosPorMesUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListarGastosPorMesUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository
) : ListarGastosPorMesUseCase {

    override fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<Gasto>> {
        validarCompetencia(mesReferencia, anoReferencia)
        return gastoRepository.listarPorMes(mesReferencia, anoReferencia)
    }
}
