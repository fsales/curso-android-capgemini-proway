package com.fsales.app.rumo.ui.feature.ganho.detalhe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsales.app.rumo.core.domain.usecase.DeletarGanhoUseCase
import com.fsales.app.rumo.core.domain.usecase.ObservarGanhoPorIdUseCase
import com.fsales.app.rumo.ui.feature.ganho.cadastro.ModoEdicaoGanho
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

@HiltViewModel(assistedFactory = DetalheGanhoViewModel.Factory::class)
class DetalheGanhoViewModel @AssistedInject constructor(
    @Assisted private val ganhoId: Long,
    private val observarGanhoPorIdUseCase: ObservarGanhoPorIdUseCase,
    private val deletarGanhoUseCase: DeletarGanhoUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(ganhoId: Long): DetalheGanhoViewModel
    }

    private val _uiState = MutableStateFlow(DetalheGanhoUiState())
    val uiState: StateFlow<DetalheGanhoUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<DetalheGanhoUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        carregarGanho()
    }

    fun onEvent(event: DetalheGanhoEvent) {
        when (event) {
            DetalheGanhoEvent.Editar -> {
                _uiEvent.trySend(
                    DetalheGanhoUiEvent.NavigateToCadastro(ganhoId, ModoEdicaoGanho.INDIVIDUAL)
                )
            }
            DetalheGanhoEvent.ExcluirEste -> _uiState.update {
                it.copy(dialogConfirmacao = TipoConfirmacaoGanho.ExcluirEste)
            }
            DetalheGanhoEvent.ExcluirTodos -> _uiState.update {
                it.copy(dialogConfirmacao = TipoConfirmacaoGanho.ExcluirTodos)
            }
            DetalheGanhoEvent.ExcluirDaquiEmDiante -> _uiState.update {
                it.copy(dialogConfirmacao = TipoConfirmacaoGanho.ExcluirDaquiEmDiante)
            }
            DetalheGanhoEvent.ConfirmarExclusao -> confirmarExclusao()
            DetalheGanhoEvent.CancelarDialog -> _uiState.update { it.copy(dialogConfirmacao = null) }
            DetalheGanhoEvent.Voltar -> _uiEvent.trySend(DetalheGanhoUiEvent.NavigateBack)
        }
    }

    private fun carregarGanho() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            observarGanhoPorIdUseCase(ganhoId)
                .catch { _uiState.update { it.copy(carregando = false, erro = "Não foi possível carregar o ganho.") } }
                .collect { ganho ->
                    if (ganho != null) {
                        _uiState.update { it.copy(ganho = ganho, carregando = false) }
                    } else {
                        _uiState.update { it.copy(carregando = false, erro = "Ganho não encontrado.") }
                    }
                }
        }
    }

    private fun confirmarExclusao() {
        viewModelScope.launch {
            _uiState.update { it.copy(excluindo = true, dialogConfirmacao = null) }
            deletarGanhoUseCase(ganhoId)
                .onSuccess { _uiEvent.send(DetalheGanhoUiEvent.NavigateBack) }
                .onFailure { _uiState.update { it.copy(excluindo = false) } }
        }
    }
}
