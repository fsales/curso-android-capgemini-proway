package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Ganho
import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.ObterGanhoPorIdUseCase
import javax.inject.Inject

class ObterGanhoPorIdUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository,
) : ObterGanhoPorIdUseCase {
    override suspend operator fun invoke(id: Long): Ganho? = ganhoRepository.buscarPorId(id)
}
