package com.fsales.app.rumo.core.domain.usecase

interface DeletarGanhoUseCase {
    suspend operator fun invoke(id: Long): Result<Unit>
}
