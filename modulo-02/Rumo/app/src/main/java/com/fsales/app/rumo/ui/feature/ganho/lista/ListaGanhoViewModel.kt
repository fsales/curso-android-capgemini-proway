package com.fsales.app.rumo.ui.feature.ganho.lista

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.ListarGanhosPorMesUseCase
import com.fsales.app.rumo.ui.ListaGanhoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ListaGanhoViewModel @Inject constructor(
    private val repository: ListarGanhosPorMesUseCase
): ViewModel() {

    private val _uiEvent = Channel<ListaGanhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListaGanhoEvent) {
        when (event) {
            is ListaGanhoEvent.Cadastro -> cadastro()
        }
    }

    private fun cadastro() {
        _uiEvent.trySend(ListaGanhoUiEvent.NavigateToCadastro)
    }
}