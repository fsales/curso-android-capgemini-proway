package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.repository.GanhoRepository
import com.fsales.app.rumo.core.domain.usecase.DeletarGanhoUseCase
import javax.inject.Inject

class DeletarGanhoUseCaseImpl @Inject constructor(
    private val ganhoRepository: GanhoRepository,
) : DeletarGanhoUseCase {
    override suspend operator fun invoke(id: Long): Result<Unit> = ganhoRepository.deletar(id)
}
