package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.ListarGanhosPorMesUseCase
import com.fsales.app.rumo.core.domain.usecase.validarCompetencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListarGanhosPorMesUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository
) : ListarGanhosPorMesUseCase {

    override fun invoke(mesReferencia: Int, anoReferencia: Int): Flow<List<Ganho>> {
        validarCompetencia(mesReferencia, anoReferencia)
        return ganhoRepository.listarPorMes(mesReferencia, anoReferencia)
    }
}
