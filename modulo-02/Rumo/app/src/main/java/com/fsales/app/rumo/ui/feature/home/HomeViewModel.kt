package com.fsales.app.rumo.ui.feature.home

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.ui.HomeUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/**
 * ViewModel da HomeScreen — shell de navegação entre abas.
 * Exceção justificada ao §13 do AGENTS.md: não injeta use case pois
 * não há regra de negócio; responsabilidade é exclusivamente gerenciar
 * o estado visual da aba ativa.
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiEvent = Channel<HomeUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.IrParaHome     -> _uiEvent.trySend(HomeUiEvent.NavigateToHome)
            HomeEvent.IrParaExtrato  -> _uiEvent.trySend(HomeUiEvent.NavigateToExtrato)
            HomeEvent.IrParaGanhos   -> _uiEvent.trySend(HomeUiEvent.NavigateToListaGanho)
            HomeEvent.IrParaGastos   -> _uiEvent.trySend(HomeUiEvent.NavigateToListaGasto)
            HomeEvent.IrParaSonhos   -> _uiEvent.trySend(HomeUiEvent.NavigateToListaSonho)
        }
    }
}
