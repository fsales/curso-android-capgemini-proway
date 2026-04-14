package com.fsales.app.rumo.core.domain.usecase

interface DeletarGastoUseCase {
    suspend operator fun invoke(id: Long): Result<Unit>
}
