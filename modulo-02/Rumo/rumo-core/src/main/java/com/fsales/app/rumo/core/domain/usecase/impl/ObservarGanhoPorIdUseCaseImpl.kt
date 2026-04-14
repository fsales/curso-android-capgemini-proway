package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.ObservarGanhoPorIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservarGanhoPorIdUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository,
) : ObservarGanhoPorIdUseCase {
    override operator fun invoke(id: Long): Flow<Ganho?> = ganhoRepository.observarPorId(id)
}
