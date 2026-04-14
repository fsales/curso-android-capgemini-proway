package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.ObterGastoPorIdUseCase
import javax.inject.Inject

class ObterGastoPorIdUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository,
) : ObterGastoPorIdUseCase {
    override suspend operator fun invoke(id: Long): Gasto? = gastoRepository.buscarPorId(id)
}
