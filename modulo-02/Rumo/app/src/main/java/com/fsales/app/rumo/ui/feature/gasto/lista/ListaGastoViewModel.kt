package com.fsales.app.rumo.ui.feature.gasto.lista

import androidx.lifecycle.ViewModel
import com.fsales.app.rumo.core.domain.usecase.ListarGastosPorMesUseCase
import com.fsales.app.rumo.ui.ListaGastoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ListaGastoViewModel @Inject constructor(
    private val repository: ListarGastosPorMesUseCase
): ViewModel() {

    private val _uiEvent = Channel<ListaGastoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

     fun onEvent(event: ListaGastoEvent) {
        when (event) {
            is ListaGastoEvent.Cadastro -> cadastro()
        }
    }

    private fun cadastro() {
        _uiEvent.trySend(ListaGastoUiEvent.NavigateToCadastro)
    }
}