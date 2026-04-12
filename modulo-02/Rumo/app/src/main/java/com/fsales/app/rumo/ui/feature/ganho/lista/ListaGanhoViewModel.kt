package com.fsales.app.rumo.ui.feature.ganho.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.usecase.ListarGanhosPorMesUseCase
import com.fsales.app.rumo.ui.ListaGanhoUiEvent
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
class ListaGanhoViewModel @Inject constructor(
    private val listarGanhosPorMesUseCase: ListarGanhosPorMesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaGanhoUiState())
    val uiState: StateFlow<ListaGanhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListaGanhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var carregarJob: Job? = null

    init {
        carregarGanhos(_uiState.value.mesAno)
    }

    fun onEvent(event: ListaGanhoEvent) {
        when (event) {
            ListaGanhoEvent.IrParaCadastro  -> cadastro()
            ListaGanhoEvent.MesAnterior     -> alterarMes(_uiState.value.mesAno.minusMonths(1))
            ListaGanhoEvent.ProximoMes      -> alterarMes(_uiState.value.mesAno.plusMonths(1))
            ListaGanhoEvent.TentarNovamente -> carregarGanhos(_uiState.value.mesAno)
        }
    }

    private fun cadastro() {
        _uiEvent.trySend(ListaGanhoUiEvent.NavigateToCadastro)
    }

    private fun alterarMes(novoMesAno: YearMonth) {
        _uiState.update { it.copy(mesAno = novoMesAno) }
        carregarGanhos(novoMesAno)
    }

    private fun carregarGanhos(mesAno: YearMonth) {
        carregarJob?.cancel()
        carregarJob = viewModelScope.launch {
            listarGanhosPorMesUseCase(mesAno.monthValue, mesAno.year)
                .onStart { _uiState.update { it.copy(carregando = true, erro = null) } }
                .catch { e ->
                    _uiState.update { it.copy(carregando = false, erro = e.message ?: "Erro desconhecido") }
                }
                .collect { ganhos ->
                    _uiState.update { it.copy(carregando = false, erro = null, ganhos = ganhos) }
                }
        }
    }
}