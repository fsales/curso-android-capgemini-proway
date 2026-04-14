package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Gasto
import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.ObservarGastoPorIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservarGastoPorIdUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository,
) : ObservarGastoPorIdUseCase {
    override operator fun invoke(id: Long): Flow<Gasto?> = gastoRepository.observarPorId(id)
}
