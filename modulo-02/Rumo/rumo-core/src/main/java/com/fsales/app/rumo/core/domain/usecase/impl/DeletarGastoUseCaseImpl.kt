package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.repository.GastoRepository
import com.fsales.app.rumo.core.domain.usecase.DeletarGastoUseCase
import javax.inject.Inject

class DeletarGastoUseCaseImpl @Inject constructor(
    private val gastoRepository: GastoRepository,
) : DeletarGastoUseCase {
    override suspend operator fun invoke(id: Long): Result<Unit> = gastoRepository.deletar(id)
}
