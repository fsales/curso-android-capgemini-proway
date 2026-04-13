package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import com.fsales.app.rumo.core.domain.usecase.ObterSonhoUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObterSonhoUseCaseImpl @Inject constructor(
    private val sonhoRepository: SonhoRepository,
) : ObterSonhoUseCase {

    override fun invoke(id: Long): Flow<Sonho?> = sonhoRepository.obterPorId(id)
}

