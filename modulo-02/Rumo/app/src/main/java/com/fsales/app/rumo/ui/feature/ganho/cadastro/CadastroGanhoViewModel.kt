package com.fsales.app.rumo.ui.feature.ganho.cadastro

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.SalvarGanhoUseCase
import com.fsales.app.rumo.ui.CadastroGanhoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CadastroGanhoViewModel @Inject constructor(
    private val salvarGanhoUseCase: SalvarGanhoUseCase
): ViewModel() {

    private val _uiEvent = Channel<CadastroGanhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()
}