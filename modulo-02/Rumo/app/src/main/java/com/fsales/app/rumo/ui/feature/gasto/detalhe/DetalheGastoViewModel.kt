package com.fsales.app.rumo.ui.feature.gasto.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.usecase.DeletarGastoUseCase
import com.fsales.app.rumo.core.domain.usecase.ObservarGastoPorIdUseCase
import com.fsales.app.rumo.ui.feature.gasto.cadastro.ModoEdicaoGasto
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetalheGastoViewModel.Factory::class)
class DetalheGastoViewModel @AssistedInject constructor(
    @Assisted private val gastoId: Long,
    private val observarGastoPorIdUseCase: ObservarGastoPorIdUseCase,
    private val deletarGastoUseCase: DeletarGastoUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(gastoId: Long): DetalheGastoViewModel
    }

    private val _uiState = MutableStateFlow(DetalheGastoUiState())
    val uiState: StateFlow<DetalheGastoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<DetalheGastoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        carregarGasto()
    }

    fun onEvent(event: DetalheGastoEvent) {
        when (event) {
            DetalheGastoEvent.Editar -> {
                _uiState.value.gasto ?: return
                _uiEvent.trySend(DetalheGastoUiEvent.NavigateToCadastro(gastoId, ModoEdicaoGasto.INDIVIDUAL))
            }
            DetalheGastoEvent.ExcluirEste -> _uiState.update {
                it.copy(dialogConfirmacao = TipoConfirmacaoGasto.ExcluirEste)
            }
            DetalheGastoEvent.ExcluirTodos -> _uiState.update {
                it.copy(dialogConfirmacao = TipoConfirmacaoGasto.ExcluirTodos)
            }
            DetalheGastoEvent.ExcluirDaquiEmDiante -> _uiState.update {
                it.copy(dialogConfirmacao = TipoConfirmacaoGasto.ExcluirDaquiEmDiante)
            }
            DetalheGastoEvent.ConfirmarExclusao -> confirmarExclusao()
            DetalheGastoEvent.CancelarDialog -> _uiState.update { it.copy(dialogConfirmacao = null) }
            DetalheGastoEvent.Voltar -> _uiEvent.trySend(DetalheGastoUiEvent.NavigateBack)
        }
    }

    private fun carregarGasto() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            observarGastoPorIdUseCase(gastoId)
                .catch { _uiState.update { it.copy(carregando = false, erro = "Não foi possível carregar o gasto.") } }
                .collect { gasto ->
                    if (gasto != null) {
                        _uiState.update { it.copy(gasto = gasto, carregando = false) }
                    } else {
                        _uiState.update { it.copy(carregando = false, erro = "Gasto não encontrado.") }
                    }
                }
        }
    }

    private fun confirmarExclusao() {
        viewModelScope.launch {
            _uiState.update { it.copy(excluindo = true, dialogConfirmacao = null) }
            deletarGastoUseCase(gastoId)
                .onSuccess { _uiEvent.send(DetalheGastoUiEvent.NavigateBack) }
                .onFailure { _uiState.update { it.copy(excluindo = false) } }
        }
    }
}
