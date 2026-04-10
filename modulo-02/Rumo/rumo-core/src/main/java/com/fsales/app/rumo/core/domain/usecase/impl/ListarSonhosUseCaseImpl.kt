package com.fsales.app.rumo.core.domain.usecase.impl

import com.fsales.app.rumo.core.domain.model.Sonho
import com.fsales.app.rumo.core.domain.repository.SonhoRepository
import com.fsales.app.rumo.core.domain.usecase.ListarSonhosUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListarSonhosUseCaseImpl @Inject constructor(
    private val sonhoRepository: SonhoRepository
) : ListarSonhosUseCase {

    override fun invoke(): Flow<List<Sonho>> = sonhoRepository.listarTodos()
}
