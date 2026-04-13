package com.fsales.app.rumo.ui.feature.sonho.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.model.calcularProjecao
import com.fsales.app.rumo.core.domain.usecase.ConcluirSonhoUseCase
import com.fsales.app.rumo.core.domain.usecase.ListarSonhosUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterSaldoMensalUseCase
import com.fsales.app.rumo.core.domain.usecase.ObterSonhoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@HiltViewModel(assistedFactory = DetalheSonhoViewModel.Factory::class)
class DetalheSonhoViewModel @AssistedInject constructor(
    @Assisted val sonhoId: Long,
    private val obterSonhoUseCase: ObterSonhoUseCase,
    private val listarSonhosUseCase: ListarSonhosUseCase,
    private val obterSaldoMensalUseCase: ObterSaldoMensalUseCase,
    private val concluirSonhoUseCase: ConcluirSonhoUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(sonhoId: Long): DetalheSonhoViewModel
    }

    private val _uiState = MutableStateFlow(DetalheSonhoUiState())
    val uiState: StateFlow<DetalheSonhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<DetalheSonhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        carregarDetalhe()
    }

    fun onEvent(event: DetalheSonhoEvent) {
        when (event) {
            DetalheSonhoEvent.Concluir           -> _uiState.update { it.copy(exibirDialogoConclusao = true) }
            DetalheSonhoEvent.CancelarConclusao  -> _uiState.update { it.copy(exibirDialogoConclusao = false) }
            DetalheSonhoEvent.ConfirmarConclusao -> confirmarConclusao()
            DetalheSonhoEvent.Voltar             -> _uiEvent.trySend(DetalheSonhoUiEvent.NavigateBack)
        }
    }

    private fun carregarDetalhe() {
        val hoje = LocalDate.now()
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }

            combine(
                obterSonhoUseCase(sonhoId),
                listarSonhosUseCase(),
                obterSaldoMensalUseCase(hoje.monthValue, hoje.year),
            ) { sonho, todosSonhos, saldo ->
                if (sonho == null) return@combine _uiState.update {
                    it.copy(carregando = false, erro = "Sonho não encontrado.")
                }

                val ativos    = todosSonhos.filter { !it.concluido }
                val pesoTotal = ativos.sumOf { it.prioridade.peso }

                val saldoAlocado = if (!sonho.concluido && pesoTotal > 0 && saldo.saldo > BigDecimal.ZERO) {
                    saldo.saldo
                        .multiply(BigDecimal(sonho.prioridade.peso))
                        .divide(BigDecimal(pesoTotal), 2, RoundingMode.HALF_UP)
                } else {
                    BigDecimal.ZERO
                }

                val projecao = sonho.calcularProjecao(saldoAlocado)
                _uiState.update { it.copy(carregando = false, erro = null, sonho = sonho, projecao = projecao) }
            }
                .catch { e ->
                    _uiState.update { it.copy(carregando = false, erro = e.message ?: "Erro desconhecido") }
                }
                .collect {}
        }
    }

    private fun confirmarConclusao() {
        val sonho = _uiState.value.sonho ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(exibirDialogoConclusao = false) }
            concluirSonhoUseCase(sonho)
                .onSuccess { _uiEvent.send(DetalheSonhoUiEvent.NavigateBack) }
                .onFailure { _uiState.update { it.copy(erro = "Não foi possível concluir o sonho. Tente novamente.") } }
        }
    }
}



