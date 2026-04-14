package com.fsales.app.rumo.ui.feature.gasto.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.usecase.ListarGastosPorMesUseCase
import com.fsales.app.rumo.ui.ListaGastoUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ListaGastoViewModel @Inject constructor(
    private val listarGastosPorMesUseCase: ListarGastosPorMesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaGastoUiState())
    val uiState: StateFlow<ListaGastoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListaGastoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var carregarJob: Job? = null

    init {
        carregarGastos(_uiState.value.mesAno)
    }

    fun onEvent(event: ListaGastoEvent) {
        when (event) {
            ListaGastoEvent.IrParaCadastro       -> cadastro()
            is ListaGastoEvent.AbrirDetalhe      -> _uiEvent.trySend(ListaGastoUiEvent.NavigateToDetalhe(event.id))
            ListaGastoEvent.MesAnterior          -> alterarMes(_uiState.value.mesAno.minusMonths(1))
            ListaGastoEvent.ProximoMes           -> alterarMes(_uiState.value.mesAno.plusMonths(1))
            is ListaGastoEvent.SelecionarMesAno  -> alterarMes(event.mesAno)
            ListaGastoEvent.TentarNovamente      -> carregarGastos(_uiState.value.mesAno)
        }
    }

    private fun cadastro() {
        _uiEvent.trySend(ListaGastoUiEvent.NavigateToCadastro)
    }

    private fun alterarMes(novoMesAno: YearMonth) {
        _uiState.update { it.copy(mesAno = novoMesAno) }
        carregarGastos(novoMesAno)
    }

    private fun carregarGastos(mesAno: YearMonth) {
        carregarJob?.cancel()
        carregarJob = viewModelScope.launch {
            listarGastosPorMesUseCase(mesAno.monthValue, mesAno.year)
                .onStart { _uiState.update { it.copy(carregando = true, erro = null) } }
                .catch { e ->
                    _uiState.update { it.copy(carregando = false, erro = e.message ?: "Erro desconhecido") }
                }
                .collect { gastos ->
                    _uiState.update { it.copy(carregando = false, erro = null, gastos = gastos) }
                }
        }
    }
}